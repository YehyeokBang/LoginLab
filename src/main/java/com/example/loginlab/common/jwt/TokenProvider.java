package com.example.loginlab.common.jwt;

import com.example.loginlab.domain.users.common.UserLevel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class TokenProvider {

    private final Key key;
    private final long tokenValidityTime;

    public TokenProvider(@Value("${jwt.secret}") String key,
                         @Value("${jwt.token-validity-in-milliseconds}") long tokenValidityTime) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityTime = tokenValidityTime;
    }

    public String createToken(String subject, UserLevel userLevel) {
        Date now = new Date();
        Date tokenExpiredTime = new Date(now.getTime() + tokenValidityTime);

        return Jwts.builder()
                .setSubject(subject)
                .claim("userLevel", userLevel)
                .setIssuedAt(now)
                .setExpiration(tokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String parseToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims;
        try {
            claims = parseClaims(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        String email = claims.getSubject();
        UserLevel userLevel = UserLevel.valueOf((String) claims.get("userLevel"));
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userLevel.name()));
        UserDetails principal = new User(email, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

}
