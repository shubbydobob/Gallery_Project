package com.project.gallery.item.service;

import com.project.gallery.item.dto.ItemRead;
import com.project.gallery.item.entity.Item;
import com.project.gallery.item.repository.ItemReposirory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseItemService implements ItemService {

    private final ItemReposirory itemReposirory;

    @Override
    public List<ItemRead> findAll() {
        return itemReposirory.findAll().stream().map(Item::toItemRead).toList();
    }

    @Override
    public List<ItemRead> findAll(List<Integer> ids) {
        return itemReposirory.findByIdIn(ids).stream().map(Item::toItemRead).toList();
    }
}
