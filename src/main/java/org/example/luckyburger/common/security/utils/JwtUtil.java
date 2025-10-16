package org.example.luckyburger.common.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.luckyburger.common.security.properties.JwtSecurityProperties;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final JwtSecurityProperties jwtSecurityProperties;

    private Key key;

    // Spring Bean이 초기화된 후 딱 한번 실행되도록 보장
    @PostConstruct
    public void init() {
        String secretKey = jwtSecurityProperties.getSecret().getKey();
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes); //jwt 라이브러리에서 사용할 수 있는 Key 객체로 변환
    }

    // 토큰 생성
    public String createToken(Long userId, String email, AccountRole role) {
        Date date = new Date();
        String PREFIX = jwtSecurityProperties.getToken().getPrefix();
        long TOKEN_TIME = jwtSecurityProperties.getToken().getExpiration();

        return PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("email", email)
                        .claim("role", role.getUserRole())
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // 토큰 prefix 제거
    public String substringToken(String bearerToken) {
        String prefix = jwtSecurityProperties.getToken().getPrefix();

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(prefix)) {
            return bearerToken.substring(prefix.length());
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 Claims 변환
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
