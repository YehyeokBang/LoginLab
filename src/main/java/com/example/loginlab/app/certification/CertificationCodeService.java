package com.example.loginlab.app.certification;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CertificationCodeService {

    @Cacheable(cacheNames = "emailCertificationCode", value = "emailCertificationCode", key = "#email")
    public String getCertificationCode(String email) {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    @CacheEvict(cacheNames = "emailCertificationCode", value = "emailCertificationCode", key = "#email")
    public void deleteCertificationCode(String email) {}

}
