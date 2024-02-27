package com.example.loginlab.domain.users.user;

import com.example.loginlab.domain.users.user.social.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmail(String email);
    @Query("SELECT u, sc FROM User u LEFT JOIN u.socialConnections sc WHERE u.email = :email AND (sc.socialType = :socialType OR sc.socialType IS NULL)")
    Optional<Object[]> findUserAndSocialConnectionByEmailAndSocialType(@Param("email") String email, @Param("socialType") SocialType socialType);

}
