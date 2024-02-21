package com.example.loginlab.app;

import com.example.loginlab.api.dto.UserDto;
import com.example.loginlab.app.encryption.EncryptionService;
import com.example.loginlab.common.error.exception.CustomException;
import com.example.loginlab.common.jwt.TokenProvider;
import com.example.loginlab.domain.users.common.UserLevel;
import com.example.loginlab.domain.users.user.User;
import com.example.loginlab.domain.users.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.loginlab.common.error.ErrorCode.INVALID_EMAIL_PASSWORD_MATCH;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    private final TokenProvider tokenProvider;

    protected User findUserByValidatingCredentials(UserDto.LoginRequest request) {
        String email = request.getEmail();
        String rawPassword = request.getPassword();

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null || !encryptionService.match(rawPassword, user.getPassword())) {
            throw new CustomException(INVALID_EMAIL_PASSWORD_MATCH);
        }

        return user;
    }

    @Transactional
    public UserDto.LoginResponse login(UserDto.LoginRequest request) {
        User user = findUserByValidatingCredentials(request);
        String email = user.getEmail();
        UserLevel level = user.getUserLevel();

        String token = tokenProvider.createToken(email, level);

        return UserDto.LoginResponse.builder()
                .token(token)
                .build();
    }

}
