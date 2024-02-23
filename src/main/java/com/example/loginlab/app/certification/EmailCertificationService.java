package com.example.loginlab.app.certification;

import com.example.loginlab.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.example.loginlab.common.error.ErrorCode.FAILED_TO_SEND_EMAIL;

@Service
@RequiredArgsConstructor
public class EmailCertificationService {

    private final JavaMailSender mailSender;

    private final CertificationCodeService certificationCodeService;

    public String sendCertificationEmail(String email) {
        String certificationCode = certificationCodeService.getCertificationCode(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[LoginLab] 인증 코드 발송");
        message.setText("인증번호: " + certificationCode);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new CustomException(FAILED_TO_SEND_EMAIL);
        }

        return message.getText();
    }

    public boolean verifyCertificationCode(String email, String inputCode) {
        String code = certificationCodeService.getCertificationCode(email);
        return code.equals(inputCode);
    }

    @CacheEvict(cacheNames = "emailCertificationCode", value = "emailCertificationCode", key = "#email")
    public void deleteCertificationCode(String email) {}

}
