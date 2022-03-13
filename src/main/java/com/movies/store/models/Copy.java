package com.movies.store.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "copies")
@Getter
@Setter
public class Copy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Movie movie;

    @OneToMany(mappedBy = "copy", fetch = FetchType.LAZY)
    private List<RentedCopy> rentedCopy;
}
