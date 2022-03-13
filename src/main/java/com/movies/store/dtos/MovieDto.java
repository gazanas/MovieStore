package com.movies.store.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MovieDto {

    private Integer id;

    private String title;

    private Integer categoryId;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
