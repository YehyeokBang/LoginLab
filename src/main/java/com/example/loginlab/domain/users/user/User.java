package com.example.loginlab.domain.users.user;

import com.example.loginlab.domain.users.common.UserBase;
import com.example.loginlab.domain.users.user.social.SocialConnection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static com.example.loginlab.domain.users.common.UserLevel.UNAUTH;
import static jakarta.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends UserBase {

    @Column(unique = true)
    private String nickname;

    private String phone;

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private Set<SocialConnection> socialConnections = new HashSet<>();

    @Builder
    public User(String email, String password, String nickname, String phone) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.userLevel = UNAUTH;
    }

    public void addSocialConnection(SocialConnection socialConnection) {
        this.socialConnections.add(socialConnection);
    }

    public String[] getSocialTypes() {
        return socialConnections.stream()
                .map(SocialConnection::getSocialType)
                .map(Enum::name)
                .toArray(String[]::new);
    }

}
