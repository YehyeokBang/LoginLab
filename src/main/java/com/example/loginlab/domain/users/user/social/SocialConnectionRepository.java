package com.example.loginlab.domain.users.user.social;

import com.example.loginlab.domain.users.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialConnectionRepository extends JpaRepository<SocialConnection, Long> {
    Optional<SocialConnection> findByUserAndSocialType(User user, SocialType socialType);
}
