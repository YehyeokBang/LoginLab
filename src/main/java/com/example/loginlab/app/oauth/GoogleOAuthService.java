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

import static com.example.loginlab.common.error.ErrorCode.FAILED_TO_GOOGLE_LOGIN;
import static org.springframework.http.HttpMethod.GET;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService implements OAuthService {

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect-url}")
    private String redirectUri;

    private final RestTemplate restTemplate;

    private HttpEntity<MultiValueMap<String, String>> createRequestEntityToGetAccessToken(String code) {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("code", code);
        requestParams.add("scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");
        requestParams.add("client_id", clientId);
        requestParams.add("client_secret", clientSecret);
        requestParams.add("redirect_uri", redirectUri);
        requestParams.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        return new HttpEntity<>(requestParams, headers);
    }

    private OAuthDto.LoginResponse parseAccessToken(ResponseEntity<String> response) {
        JsonElement element = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();

        String accessToken = element.getAsJsonObject()
                .get("access_token")
                .getAsString();

        return OAuthDto.LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public OAuthDto.LoginResponse getAccessToken(String code) {
        String requestUrl = "https://oauth2.googleapis.com/token";

        HttpEntity<MultiValueMap<String, String>> requestEntity = createRequestEntityToGetAccessToken(code);
        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(FAILED_TO_GOOGLE_LOGIN);
        }

        return parseAccessToken(response);
    }

    private HttpEntity<MultiValueMap<String, String>> createRequestEntityToGetUserInfo() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        return new HttpEntity<>(headers);
    }

    private OAuthDto.UserResponse parseUserInfo(ResponseEntity<String> response) {
        JsonElement element = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();

        String id = element.getAsJsonObject()
                .get("id")
                .getAsString();

        String email = element.getAsJsonObject()
                .get("email")
                .getAsString();

        String nickname = element.getAsJsonObject()
                .get("name")
                .getAsString();

        return OAuthDto.UserResponse.builder()
                .socialUid(id)
                .socialType("GOOGLE")
                .email(email)
                .nickname(nickname)
                .build();
    }

    @Override
    public OAuthDto.UserResponse getUserInfo(String accessToken) {
        String requestUrl = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;

        HttpEntity<MultiValueMap<String, String>> requestEntity = createRequestEntityToGetUserInfo();
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, GET, requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(FAILED_TO_GOOGLE_LOGIN);
        }

        return parseUserInfo(response);
    }

}
