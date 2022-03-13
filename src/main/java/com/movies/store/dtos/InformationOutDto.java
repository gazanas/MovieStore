package com.movies.store.dtos;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class InformationOutDto {

    private Integer id;

    private String summary;

    private Timestamp released;

    private Double rating;

    private List<ActorOutDto> actors;

    private List<DirectorOutDto> directors;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
