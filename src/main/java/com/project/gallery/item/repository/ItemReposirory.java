package com.project.gallery.item.repository;

import com.project.gallery.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemReposirory extends JpaRepository <Item, Integer> {

    // 여러 아이디로 상품을 조회
    List<Item> findByIdIn(List<Integer> ids);
}
