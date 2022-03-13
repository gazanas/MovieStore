package com.movies.store.services;

import com.movies.store.exceptions.CopyNotFoundException;
import com.movies.store.models.Copy;
import com.movies.store.models.RentedCopy;
import com.movies.store.models.User;
import com.movies.store.repositories.RentedCopyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentedCopyService {

    private final RentedCopyRepository rentedCopyRepository;

    private final UserService userService;

    private final ChargeService chargeService;

    /**
     * If a movie copy is rented then get the rented movie copy
     *
     * @param copyId The id of the movie copy
     * @return If the copy is rented return this rented copy else return null
     */
    public RentedCopy getRentedCopy(Integer copyId) {
        return rentedCopyRepository.findByCopyIdAndReturnedAtIsNull(copyId)
                .orElse(null);
    }

    /**
     * Add a new entry in the rented movie copies for the given movie copy requested
     *
     * @param copy The given movie copy to be rented
     * @return The saved rented copy entity
     */
    public RentedCopy rentCopyOfAMovie(Copy copy) {
        RentedCopy rentedCopy = new RentedCopy();
        rentedCopy.setCopy(copy);
        rentedCopy.setRentedAt(Timestamp.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime()));
        User user = this.userService.getUserByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        rentedCopy.setUser(user);
        return this.rentedCopyRepository.save(rentedCopy);
    }

    /**
     * If the request copy to be returned is in fact rented the update the status
     * of the rented copy so that it is not considered rented anymore. Updating
     * this status is being done by setting the return date
     *
     * @param copyId The given movie copy to be returned
     */
    public RentedCopy setReturnedStatus(Integer copyId) {
        RentedCopy rentedCopy = this.rentedCopyRepository.findByCopyIdAndReturnedAtIsNull(copyId)
                .orElseThrow(() -> new CopyNotFoundException("Rented copy could not be found!"));

        Timestamp returnedAt = Timestamp.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime());
        rentedCopy.setReturnedAt(returnedAt);
        rentedCopy.setCharge(this.chargeService.addChargeForReturnedCopy(rentedCopy));
        return this.rentedCopyRepository.save(rentedCopy);
    }

    /**
     * Get the rented movie copies by the current user
     *
     * @return A list containing the rented movie copies by the current user
     */
    public List<RentedCopy> getUsersRentedMovies() {
        try {
            String authenticated;
            try {
                authenticated = SecurityContextHolder.getContext().getAuthentication().getName();
            } catch (NullPointerException e) {
                throw new AccessDeniedException("User is not authenticated");
            }
            User user = this.userService.getUserByUsername(authenticated);

            return user.getRentedCopies().stream().filter((RentedCopy copy) -> copy.getReturnedAt() == null)
                    .collect(Collectors.toList());
        } catch (UsernameNotFoundException e) {
            throw new AccessDeniedException("User is not authenticated");
        }
    }
}
