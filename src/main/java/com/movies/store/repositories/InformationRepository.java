package com.movies.store.repositories;

import com.movies.store.models.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InformationRepository extends JpaRepository<Information, Integer> {

    Optional<Information> findByMovieId(@Param("movieId") Integer movieId);
}
