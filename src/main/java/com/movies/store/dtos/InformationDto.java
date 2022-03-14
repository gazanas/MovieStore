package com.movies.store.dtos;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class InformationDto {

    private Integer id;

    private String summary;

    private Timestamp released;

    private Double rating;

    private List<ActorDto> actors;

    private List<DirectorDto> directors;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
