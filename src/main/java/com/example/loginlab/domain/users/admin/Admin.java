package com.example.loginlab.domain.users.admin;

import com.example.loginlab.domain.users.common.UserBase;
import com.example.loginlab.domain.users.common.UserLevel;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends UserBase {

    @Builder
    public Admin(String email, String password, UserLevel userLevel) {
        this.email = email;
        this.password = password;
        this.userLevel = userLevel;
    }

}
