package com.example.loginlab.api.dto;

import com.example.loginlab.app.encryption.EncryptionService;
import com.example.loginlab.domain.users.common.UserLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpRequest {

        @NotBlank(message = "이메일 주소를 입력해주세요.")
        @Email(message = "정확한 이메일 주소를 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        private String password;

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
        private String nickname;

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
        private String phone;

        @Builder
        public SignUpRequest(String email, String password, String nickname, String phone) {
            this.email = email;
            this.password = password;
            this.nickname = nickname;
            this.phone = phone;
        }

        public void passwordEncryption(EncryptionService encryptionService) {
            this.password = encryptionService.encrypt(password);
        }

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginRequest {

        @NotBlank(message = "이메일 주소를 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;

        public void passwordEncryption(EncryptionService encryptionService) {
            this.password = encryptionService.encrypt(password);
        }

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginResponse {

        private String token;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserResponse {

        private Long id;

        private String email;

        private String nickname;

        private String phone;

        private String userLevel;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserSearchRequest {

        private Long id;

        private String email;

        private String nickname;

        private UserLevel userLevel;

        @Builder
        public UserSearchRequest(Long id, String email, String nickname, UserLevel userLevel) {
            this.id = id;
            this.email = email;
            this.nickname = nickname;
            this.userLevel = userLevel;
        }

    }

}
