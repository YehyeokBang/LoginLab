package com.example.loginlab.common.error;

import com.example.loginlab.common.error.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static com.example.loginlab.common.error.ErrorCode.UNAUTHORIZED_USER;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try {
            ObjectMapper mapper = new ObjectMapper();
            ErrorResponse errorResponse = ErrorResponse.of(UNAUTHORIZED_USER);
            String json = mapper.writeValueAsString(errorResponse);

            OutputStream out = response.getOutputStream();
            out.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("AuthenticationEntryPoint error", e);
        }
    }

}
