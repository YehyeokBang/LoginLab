package com.example.loginlab.domain.users.common;

import com.example.loginlab.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.loginlab.domain.users.common.UserLevel.UNAUTH;
import static com.example.loginlab.domain.users.common.UserLevel.USER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED) // 상속 관계를 가지는 각 엔티티마다 테이블을 만들고, 공통된 속성은 부모 테이블에 만들어서 조인하는 전략
public abstract class UserBase extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(unique = true)
    protected String email;

    protected String password;

    @Enumerated(EnumType.STRING)
    protected UserLevel userLevel;

    public void certify() {
        if (this.userLevel == UNAUTH) {
            this.userLevel = USER;
        }
    }

}
