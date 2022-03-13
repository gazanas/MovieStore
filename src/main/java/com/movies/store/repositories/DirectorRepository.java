package com.movies.store.repositories;

import com.movies.store.models.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {

    /**
     * Find a director entity when the input matches either their first name
     * or their last name
     *
     * @param name The requested name to be searched
     * @return A list of directors whose name matched the criteria
     */
    @Query("SELECT d FROM Director d WHERE d.firstName LIKE %:name% OR d.lastName LIKE %:name%")
    List<Director> findByFirstNameOrLastName(@Param("name") String name);
}
