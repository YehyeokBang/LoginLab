package com.example.loginlab.app;

import com.example.loginlab.api.dto.UserDto;
import com.example.loginlab.app.encryption.EncryptionService;
import com.example.loginlab.common.error.exception.CustomException;
import com.example.loginlab.domain.users.user.User;
import com.example.loginlab.domain.users.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
        CustomException exception = assertThrows(CustomException.class, () -> userService.save(request));

        // then
        assertEquals("DUPLICATE_USER_EMAIL", exception.getErrorCode().name());
    }

    @Test
    @DisplayName("이미 사용중인 닉네임으로 가입 불가")
    void saveWithDuplicateNickname() {
        // given
        UserDto.SignUpRequest request = createUserDto();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByNickname(request.getNickname())).thenReturn(true);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> userService.save(request));

        // then
        assertEquals("DUPLICATE_USER_NICKNAME", exception.getErrorCode().name());
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

    @Test
    @DisplayName("이메일로 사용자 조회 실패")
    void findByEmailFail() {
        // given
        String email = "";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> userService.findByEmail(email));

        // then
        assertEquals("NOT_FOUND_USER", exception.getErrorCode().name());
    }

    @Test
    @DisplayName("이메일로 사용자 조회 성공")
    void findByEmail() {
        // given
        String email = "test@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User(email, "testpassword", "tester", "01012345678")));

        // when
        UserDto.UserResponse userResponse = userService.findByEmail(email);

        // then
        assertEquals(email, userResponse.getEmail());
    }

}
