package com.example.loginlab.app.encryption;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptEncryptionService implements EncryptionService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BcryptEncryptionService() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encrypt(String input) {
        return bCryptPasswordEncoder.encode(input);
    }

    @Override
    public boolean match(String input, String encrypted) {
        return bCryptPasswordEncoder.matches(input, encrypted);
    }

}
