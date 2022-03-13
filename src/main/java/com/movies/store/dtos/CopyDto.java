package com.movies.store.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CopyDto {

    private Integer id;

    private Integer movieId;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
