package com.project.gallery.block.repository;

import com.project.gallery.block.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Integer> {

    // 블록된 토큰 조회
    Optional<Block> findByToken(String token);

}
