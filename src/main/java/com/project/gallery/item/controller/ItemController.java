package com.project.gallery.item.controller;

import com.project.gallery.item.dto.ItemRead;
import com.project.gallery.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "상품", description = "상품")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "상품", description = "상품 목록 조회")
    @GetMapping("/api/items")
    public ResponseEntity<?> readAll() {
        List<ItemRead> items = itemService.findAll();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
