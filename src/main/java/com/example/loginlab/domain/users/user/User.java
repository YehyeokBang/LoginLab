package com.example.loginlab.domain.users.user;

import com.example.loginlab.domain.users.common.UserBase;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.loginlab.domain.users.common.UserLevel.UNAUTH;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends UserBase {

    private String nickname;

    private String phone;

    @Builder
    public User(String email, String password, String nickname, String phone) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.userLevel = UNAUTH;
    }

}
