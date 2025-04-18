package com.project.gallery.block.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "blocks")
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    @CreationTimestamp
    private LocalDateTime created;

    public Block() {

    }

    public Block(String token) {
        this.token = token;
    }
}
