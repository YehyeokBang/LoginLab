package com.example.loginlab.app.oauth;

import com.example.loginlab.api.dto.OAuthDto;
import com.example.loginlab.common.error.exception.CustomException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.example.loginlab.common.error.ErrorCode.FAILED_TO_KAKAO_LOGIN;
import static org.springframework.http.HttpMethod.POST;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {

    @Value("${kakao.auth.rest-api-key}")
    private String restApiKey;

    @Value("${kakao.auth.redirect-url}")
    private String redirectUri;

    private final RestTemplate restTemplate;

    private HttpEntity<MultiValueMap<String, String>> createRequestEntityToGetAccessToken(String code) {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("grant_type", "authorization_code");
        requestParams.add("client_id", restApiKey);
        requestParams.add("redirect_uri", redirectUri);
        requestParams.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        return new HttpEntity<>(requestParams, headers);
    }

    private OAuthDto.LoginResponse parseAccessToken(ResponseEntity<String> response) {
        JsonElement element = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();

        String accessToken = element.getAsJsonObject()
                .get("access_token")
                .getAsString();

        String refreshToken = element.getAsJsonObject()
                .get("refresh_token")
                .getAsString();

        return OAuthDto.LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public OAuthDto.LoginResponse getAccessToken(String code) {
        String requestUrl = "https://kauth.kakao.com/oauth/token";
        HttpEntity<MultiValueMap<String, String>> requestEntity = createRequestEntityToGetAccessToken(code);

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, POST, requestEntity, String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(FAILED_TO_KAKAO_LOGIN);
        }

        return parseAccessToken(responseEntity);
    }

    private HttpEntity<MultiValueMap<String, String>> createRequestEntityToGetUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + accessToken);

        return new HttpEntity<>(headers);
    }

    private OAuthDto.UserResponse parseUserInfo(ResponseEntity<String> response) {
        JsonElement element = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();

        String socialUid = element.getAsJsonObject()
                .get("id")
                .getAsString();

        String email = element.getAsJsonObject()
                .get("kakao_account")
                .getAsJsonObject()
                .get("email")
                .getAsString();

        return OAuthDto.UserResponse.builder()
                .socialUid(socialUid)
                .socialType("KAKAO")
                .email(email)
                .build();
    }

    @Override
    public OAuthDto.UserResponse getUserInfo(String accessToken) {
        String requestUrl = "https://kapi.kakao.com/v2/user/me";
        HttpEntity<MultiValueMap<String, String>> requestEntity = createRequestEntityToGetUserInfo(accessToken);

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, POST, requestEntity, String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(FAILED_TO_KAKAO_LOGIN);
        }

        return parseUserInfo(responseEntity);
    }

}
