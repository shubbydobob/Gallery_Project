package com.project.gallery.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class HashUtils {

    public static String generateSalt(int size) {
        char[] resultArr = new char[size];
        Random random = new Random();

        String options = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" +
                "0123456789" + "!@#$%^&*()_+";

        for (int i = 0; i < resultArr.length; i+= 1) {
            resultArr[i] = options.charAt(random.nextInt(options.length()));
        }
        return new String(resultArr);
    }
    
    // 해시 데이터 생성
    public static String generateHash(String value, String salt) {
        try {
            // SHA-256 해시 알고리즘을 사용하여 해시값 생성
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 원본 값과 솔트 합치기
            String passwordSalted = value + salt;

            // 문자열 데이터 해싱
            byte[] hashBytes = md.digest(passwordSalted.getBytes(StandardCharsets.UTF_8));

            // 바이트 배열을 Base64로 인코딩해서 반환
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException ex) {
            // SHA-256 알고리즘을 찾을 수 없는 경우 예외 처리
            throw new RuntimeException("SHA-256 algorithm not found", ex);
        }
    }
}
