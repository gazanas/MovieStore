package com.movies.store.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "charges")
@Getter
@Setter
public class Charge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double amount;

    private Boolean paid;

    @OneToOne
    private RentedCopy rentedCopy;
}
