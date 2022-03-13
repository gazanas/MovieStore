package com.movies.store.repositories;

import com.movies.store.models.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Integer> {

    List<Charge> findByRentedCopyUserUsername(@Param("username") String username);
}
