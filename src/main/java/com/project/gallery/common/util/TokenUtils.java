package com.project.gallery.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private static final Key signKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);;


//    static {
//        //외부에 노출되면 안 되는 중요한 보안 키(32바이트 이상)
//        String base64Key = "bXlfc3VwZXJfc2VjdXJlX2tleV9mb3Jfand0X3VzZQ=="; // 44자 base64 문자열 = 32바이트 원본
//        byte[] secretKeyBytes = Base64.getDecoder().decode(base64Key);
//        Key signKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 안전한 256비트 이상 키 생성
//    }

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
                .setClaims(claims)
                .setExpiration(expTime)
                .signWith(signKey, SignatureAlgorithm.HS256);

        return builder.compact();
    }
    public static boolean isTokenValid(String token) {
        if(StringUtils.hasLength(token)) {
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
