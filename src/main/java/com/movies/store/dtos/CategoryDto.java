package com.movies.store.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CategoryDto {

    private Integer id;

    private String category;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
