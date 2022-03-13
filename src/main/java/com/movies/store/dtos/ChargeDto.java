package com.movies.store.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChargeDto {

    private Integer id;

    private Double amount;

    private Boolean paid;

    private Integer copyId;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
