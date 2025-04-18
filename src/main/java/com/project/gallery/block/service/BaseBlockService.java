package com.project.gallery.block.service;

import com.project.gallery.block.entity.Block;
import com.project.gallery.block.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseBlockService implements BlockService {
    
    private final BlockRepository blockRepository;
    
    // 블록된 토큰 저장
    @Override
    public void saveBlockToken(String token) {
        blockRepository.save(new Block(token));
    }

    // 블록된 토큰 조회
    @Override
    public boolean isTokenBlocked(String token) {
        return blockRepository.findByToken(token).isPresent();
    }
}
