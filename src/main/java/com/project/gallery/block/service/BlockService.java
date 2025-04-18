package com.project.gallery.block.service;

public interface BlockService {

    // 블록된 토큰 저장
    void saveBlockToken(String token);

    // 블록된 토큰 조회
    boolean isTokenBlocked(String token);


}
