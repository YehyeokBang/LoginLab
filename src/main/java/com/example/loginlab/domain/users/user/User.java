package com.example.loginlab.domain.users.user;

import com.example.loginlab.domain.users.common.UserBase;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends UserBase {

    private String nickname;

    private String phone;

}
