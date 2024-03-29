package com.example.loginlab.app;

import com.example.loginlab.api.dto.UserDto;
import com.example.loginlab.app.certification.EmailCertificationService;
import com.example.loginlab.app.encryption.EncryptionService;
import com.example.loginlab.common.error.exception.CustomException;
import com.example.loginlab.domain.users.user.User;
import com.example.loginlab.domain.users.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.loginlab.common.error.ErrorCode.DUPLICATE_USER_EMAIL;
import static com.example.loginlab.common.error.ErrorCode.DUPLICATE_USER_NICKNAME;
import static com.example.loginlab.common.error.ErrorCode.INVALID_CERTIFICATION_CODE;
import static com.example.loginlab.common.error.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    private final EmailCertificationService emailCertificationService;

    private boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void save(UserDto.SignUpRequest request) {
        if (checkEmailDuplicate(request.getEmail())) {
            throw new CustomException(DUPLICATE_USER_EMAIL);
        }
        if (checkNicknameDuplicate(request.getNickname())) {
            throw new CustomException(DUPLICATE_USER_NICKNAME);
        }

        request.passwordEncryption(encryptionService);

        userRepository.save(User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .build());
    }

    @Transactional(readOnly = true)
    public UserDto.UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        return UserDto.UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .userLevel(user.getUserLevel().toString())
                .socialTypes(user.getSocialTypes())
                .build();
    }

    public String sendCertificationEmail(String email) {
        return emailCertificationService.sendCertificationEmail(email);
    }

    @Transactional
    public String verifyCertificationCode(String email, String inputCode) {
        if (emailCertificationService.verifyCertificationCode(email, inputCode)) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

            user.certify();

            emailCertificationService.deleteCertificationCode(email);

            return "인증에 성공했습니다.";
        }

        throw new CustomException(INVALID_CERTIFICATION_CODE);
    }

}
