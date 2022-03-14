package com.movies.store.repositories;

import com.movies.store.models.Actor;
import com.movies.store.models.Director;
import com.movies.store.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    /**
     * Search the movies by the parameters passed. If a parameter is null then it is ignored
     * in the query.
     *
     * @param title
     * @param category
     * @param released
     * @param rating
     * @param actors
     * @param directors
     * @return A list of the resulted movies
     */
    @Query(value = "SELECT DISTINCT(m) FROM Movie m JOIN m.category c LEFT JOIN m.information i LEFT JOIN i.actors a " +
            "LEFT JOIN i.directors d WHERE (:title IS NULL OR (m.title LIKE %:title%)) AND " +
            "(:category IS NULL OR (c.category = :category)) AND (:released IS NULL OR (i.released = :released)) " +
            "AND (:rating IS NULL OR (i.rating = :rating)) AND " +
            "(COALESCE(:actors) IS NULL OR (a IN (SELECT actor FROM Actor actor WHERE actor IN :actors))) AND " +
            "(COALESCE(:directors) IS NULL OR (d IN (SELECT director FROM Director director WHERE director IN :directors)))")
    List<Movie> searchMovie(@Param("title") String title, @Param("category") String category,
                                        @Param("released") Timestamp released, @Param("rating") Double rating,
                                        @Param("actors") List<Actor> actors, @Param("directors") List<Director> directors);
}
