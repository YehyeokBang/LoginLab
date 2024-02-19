package com.example.loginlab.app.encryption;

public interface EncryptionService {

    String encrypt(String input);
    boolean match(String input, String encrypted);

}
