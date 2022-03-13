package com.movies.store.repositories;

import com.movies.store.models.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

    /**
     * Find an actor entity when the input matches either their first name
     * or their last name
     *
     * @param name The requested name to be searched
     * @return A list of actors whose name matched the criteria
     */
    @Query("SELECT a FROM Actor a WHERE a.firstName LIKE %:name% OR a.lastName LIKE %:name%")
    List<Actor> findByFirstNameOrLastName(@Param("name") String name);
}
