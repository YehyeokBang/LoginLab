package com.example.loginlab.api;

import com.example.loginlab.api.dto.OAuthDto;
import com.example.loginlab.app.oauth.KakaoOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    @GetMapping("kakao/callback")
    public ResponseEntity<OAuthDto.LoginResponse> kakaoCallback(@RequestParam String code) {
        return ResponseEntity.ok(kakaoOAuthService.getAccessToken(code));
    }

    @PostMapping("kakao/user")
    public ResponseEntity<OAuthDto.UserResponse> kakaoUser(@RequestParam String accessToken) {
        return ResponseEntity.ok(kakaoOAuthService.getUserInfo(accessToken));
    }

}
