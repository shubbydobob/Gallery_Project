package com.project.gallery.block.service;

public interface BlockService {

    // 토큰 차단 데이터 삽입
    void add(String token); // ①

    // 토큰 차단 데이터가 있는지 확인
    boolean has(String token); // ②


}
