package com.example.loginlab.app;

import com.example.loginlab.api.dto.UserDto;
import com.example.loginlab.app.encryption.EncryptionService;
import com.example.loginlab.domain.users.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    EncryptionService encryptionService;

    @InjectMocks
    UserService userService;

    private UserDto.SignUpRequest createUserDto() {
        return UserDto.SignUpRequest.builder()
                .email("test@gmail.com")
                .password("testpassword")
                .nickname("tester")
                .phone("01012345678")
                .build();
    }

    @Test
    @DisplayName("이미 사용중인 이메일 주소로 가입 불가")
    void saveWithDuplicateEmail() {
        // given
        UserDto.SignUpRequest request = createUserDto();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.save(request));

        // then
        assertEquals("이미 사용중인 이메일 주소입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("이미 사용중인 닉네임으로 가입 불가")
    void saveWithDuplicateNickname() {
        // given
        UserDto.SignUpRequest request = createUserDto();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByNickname(request.getNickname())).thenReturn(true);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.save(request));

        // then
        assertEquals("이미 사용중인 닉네임입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("회원 가입 성공")
    void save() {
        // given
        UserDto.SignUpRequest request = createUserDto();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByNickname(request.getNickname())).thenReturn(false);

        // when
        userService.save(request);

        // then
        // no exception
    }

    @Test
    @DisplayName("비밀번호 암호화 성공")
    void passwordEncryption() {
        // given
        UserDto.SignUpRequest request = createUserDto();
        String encryptedPassword = "encryptedPassword";
        when(encryptionService.encrypt(request.getPassword())).thenReturn(encryptedPassword);

        // when
        request.passwordEncryption(encryptionService);

        // then
        assertEquals(encryptedPassword, request.getPassword());
    }

}
