package com.project.gallery.block.service;

public interface BlockService {

    // 블록된 토큰 저장
    void add(String token);

    // 블록된 토큰 조회
    boolean has(String token);


}
