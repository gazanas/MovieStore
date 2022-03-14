package com.movies.store.configurations;

import com.movies.store.exceptions.ReturnCopyException;
import com.movies.store.models.RentedCopy;
import com.movies.store.repositories.RentedCopyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Component("ownershipFilter")
@RequiredArgsConstructor
public class OwnershipFilter {

    private final RentedCopyRepository rentedCopyRepository;

    /**
     * Checks if the copy is rented by the current user
     *
     * @param copyId
     * @return The decision of the ownership
     */
    public boolean checkIfCopyIsRentedByCurrentUser(Integer copyId) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = rentedCopyRepository.findByCopyIdAndReturnedAtIsNull(copyId)
                .orElseThrow(() -> new ReturnCopyException("You have not rented this copy!")).getUser().getUsername();

        if (!username.equals(principal)) {
            throw new ReturnCopyException("You have not rented this copy!");
        }

        return true;
    }

    /**
     * Checks if there is a copy of the movie that is rented by the current user
     *
     * @param movieId
     * @return The decision of the ownership
     */
    public boolean checkIfMovieIsRentedByCurrentUser(Integer movieId) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<RentedCopy> rentedCopyList = rentedCopyRepository.findByCopyMovieIdAndReturnedAtIsNull(movieId);

        if (rentedCopyList.isEmpty()) {
            throw new ReturnCopyException("You have not rented this movie!");
        }

        RentedCopy rentedCopy = rentedCopyList.stream().filter((RentedCopy rc) ->
                        rc.getUser().getUsername().equals(principal))
                .findFirst().orElseThrow(() -> new ReturnCopyException("You have not rented this movie!"));

        return true;
    }
}
