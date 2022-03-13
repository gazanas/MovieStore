package com.movies.store.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "movie_info")
@Getter
@Setter
public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private Movie movie;

    private String summary;

    private Timestamp released;

    private Double rating;

    @ManyToMany(mappedBy = "information", fetch = FetchType.LAZY)
    private List<Actor> actors;

    @ManyToMany(mappedBy = "information", fetch = FetchType.LAZY)
    private List<Director> directors;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
