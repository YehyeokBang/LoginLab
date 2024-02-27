package com.example.loginlab.app.oauth;

import com.example.loginlab.api.dto.OAuthDto;

public interface OAuthService {

    OAuthDto.LoginResponse getAccessToken(String code);
    OAuthDto.UserResponse getUserInfo(String accessToken);

}
