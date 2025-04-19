package com.project.gallery.common.util;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private static final Key signKey;

    static {
        //외부에 노출되면 안 되는 중요한 보안 키(32바이트 이상)
        String secretKey = "SECURITY_KEY_202504192020581601_!!";
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        signKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    // 토큰 발급
    public static String generate(String subject, String name, Object value, int expMinutes) {

        // 만료 시간 설정
        Date expTime = new Date();

        // 분(minute)을 밀리초(milliseconds)로 변환해 입력
        expTime.setTime(expTime.getTime() + 1000L * 60 * expMinutes);

        // 기본 정보 입력
        HashMap<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        // 클레임 입력
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(name, value);

        // 토큰 발급
        JwtBuilder builder = Jwts.builder()
                .setHeader(headerMap)
                .setSubject(subject)
                .setExpiration(expTime)
                .setClaims(claims)
                .signWith(signKey, SignatureAlgorithm.HS256);

        return builder.compact();
    }

    public static boolean isValid(String token) {
        if (StringUtils.hasLength(token)) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(signKey)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (ExpiredJwtException e) {
            } catch (JwtException e) { // 토큰 유효성 검사 실패
            }
        }
        return false;
    }

    // 토큰에서 클레임 정보 추출
    public static Map<String, Object> getBody(String token) {
       return Jwts.parserBuilder()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
