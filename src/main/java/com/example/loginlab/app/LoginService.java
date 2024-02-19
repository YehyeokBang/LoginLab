package com.example.loginlab.app;

import com.example.loginlab.api.dto.UserDto;
import com.example.loginlab.app.encryption.EncryptionService;
import com.example.loginlab.common.jwt.TokenProvider;
import com.example.loginlab.domain.users.common.UserLevel;
import com.example.loginlab.domain.users.user.User;
import com.example.loginlab.domain.users.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public User findUser(UserDto.LoginRequest request) {
        String email = request.getEmail();
        String rawPassword = request.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 주소 또는 비밀번호가 일치하지 않습니다."));

        if (!encryptionService.match(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("이메일 주소 또는 비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    @Transactional
    public UserDto.LoginResponse login(UserDto.LoginRequest request) {
        User user = findUser(request);
        String email = user.getEmail();
        UserLevel level = user.getUserLevel();

        String token = tokenProvider.createToken(email, level);

        return UserDto.LoginResponse.builder()
                .token(token)
                .build();
    }

}
