package com.movies.store.repositories;

import com.movies.store.models.RentedCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentedCopyRepository extends JpaRepository<RentedCopy, Integer> {

    Optional<RentedCopy> findByCopyIdAndReturnedAtIsNull(@Param("copyId") Integer copyId);
}
