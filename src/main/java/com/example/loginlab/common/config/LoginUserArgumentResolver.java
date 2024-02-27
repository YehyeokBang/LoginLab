package com.example.loginlab.common.config;

import com.example.loginlab.common.annotation.CurrentUser;
import com.example.loginlab.common.error.exception.CustomException;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

import static com.example.loginlab.common.error.ErrorCode.UNAUTHORIZED_USER;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Principal principal = webRequest.getUserPrincipal();
        if (principal == null) {
            throw new CustomException(UNAUTHORIZED_USER);
        }

        return principal.getName();
    }

}
