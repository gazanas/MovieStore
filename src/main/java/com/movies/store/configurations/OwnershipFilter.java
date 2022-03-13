package com.movies.store.configurations;

import com.movies.store.exceptions.ReturnCopyException;
import com.movies.store.repositories.RentedCopyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Transactional
@Component("ownershipFilter")
@RequiredArgsConstructor
public class OwnershipFilter {

    private final RentedCopyRepository rentedCopyRepository;

    public boolean checkIfCopyIsRentedByCurrentUser(Integer copyId) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = rentedCopyRepository.findByCopyIdAndReturnedAtIsNull(copyId)
                .orElseThrow(() -> new ReturnCopyException("You have not rented this copy!")).getUser().getUsername();

        if (!username.equals(principal)) {
            throw new ReturnCopyException("You have not rented this copy!");
        }

        return true;
    }
}
