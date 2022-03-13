package com.movies.store.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ActorOutDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
