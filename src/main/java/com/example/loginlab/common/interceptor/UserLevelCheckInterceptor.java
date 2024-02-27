package com.example.loginlab.common.interceptor;

import com.example.loginlab.common.annotation.UserLevelCheck;
import com.example.loginlab.common.error.exception.CustomException;
import com.example.loginlab.domain.users.user.User;
import com.example.loginlab.domain.users.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.Principal;

import static com.example.loginlab.common.error.ErrorCode.FORBIDDEN_USER_LEVEL;
import static com.example.loginlab.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.loginlab.common.error.ErrorCode.UNAUTHORIZED_USER;

@Component
@RequiredArgsConstructor
public class UserLevelCheckInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        UserLevelCheck userLevelCheck = handlerMethod.getMethodAnnotation(UserLevelCheck.class);

        if (userLevelCheck == null) {
            return true;
        }

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            throw new CustomException(UNAUTHORIZED_USER);
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        // UNAUTH : 0, USER : 1, ADMIN : 2
        if (user.getUserLevel().ordinal() < userLevelCheck.level().ordinal()) {
            throw new CustomException(FORBIDDEN_USER_LEVEL);
        }

        return true;
    }
}
