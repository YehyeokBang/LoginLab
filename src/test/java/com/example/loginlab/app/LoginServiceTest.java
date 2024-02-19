package com.example.loginlab.app;

import com.example.loginlab.api.dto.UserDto;
import com.example.loginlab.app.encryption.EncryptionService;
import com.example.loginlab.common.jwt.TokenProvider;
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
class LoginServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    EncryptionService encryptionService;

    @InjectMocks
    LoginService loginService;

    private UserDto.LoginRequest createLoginRequest() {
        return UserDto.LoginRequest.builder()
                .email("test@gmail.com")
                .password("testpassword")
                .build();
    }

    @Test
    @DisplayName("일치하지 않는 이메일 주소와 비밀번호로 로그인 불가")
    void loginFail() {
        // given
        UserDto.LoginRequest request = createLoginRequest();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> loginService.findUser(request));
    }

    @Test
    @DisplayName("일치하는 이메일 주소와 비밀번호로 로그인 가능")
    void loginSuccess() {
        // given
        UserDto.LoginRequest request = createLoginRequest();
        User user = User.builder()
                .email(request.getEmail())
                .password("hashedPassword")
                .build();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(encryptionService.match(request.getPassword(), user.getPassword())).thenReturn(true);

        // when
        User result = loginService.findUser(request);

        // then
        assertNotNull(result);
        assertEquals(request.getEmail(), result.getEmail());
    }

}
