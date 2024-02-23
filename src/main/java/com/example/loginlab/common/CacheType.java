package com.example.loginlab.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheType {

    EMAIL_AUTH_CODE("emailCertificationCode", 60 * 60 * 24, 1000);

    private final String cacheName;

    private final int expireAfterWrite;

    private final int maximumSize;

}
