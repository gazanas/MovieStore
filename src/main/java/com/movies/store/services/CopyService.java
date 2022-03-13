package com.movies.store.services;

import com.movies.store.dtos.CopyDto;
import com.movies.store.exceptions.NoAvailableCopyException;
import com.movies.store.mappers.CopyMapper;
import com.movies.store.models.Copy;
import com.movies.store.models.RentedCopy;
import com.movies.store.repositories.CopyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CopyService {

    private final CopyRepository copyRepository;

    private final RentedCopyService rentedCopyService;

    private final CopyMapper copyMapper;

    /**
     * Get the available copies of all the movies by getting a list of all the rented copies
     * and removing each rented copy from the copies
     *
     * @return A list containing all the available copies for each movie
     */
    public List<Copy> getAllAvailableCopies() {
        List<Copy> copies = copyRepository.findAll();
        List<Copy> rentedCopies = copies.stream()
                .filter((Copy copy) -> this.rentedCopyService.getRentedCopy(copy.getId()) != null)
                .collect(Collectors.toList());

        return copies.stream().filter((Copy copy) -> !rentedCopies.contains(copy))
                .collect(Collectors.toList());
    }

    /**
     * Get all the rented copies by the current user
     *
     * @return A list containing all the rented copies by the current user
     */
    public List<CopyDto> getUsersRentedCopies() {
        return rentedCopyService.getUsersRentedMovies().stream()
                .map(this.copyMapper::rentedCopyToDto).collect(Collectors.toList());
    }

    /**
     * If there is an available copy of the requested movie add a new entry of the this copy
     * in the rented copies for the current user
     *
     * @param movieId The id of the requested movie
     */
    public Copy rentCopy(Integer movieId) {
        List<Copy> availableCopies = this.getAllAvailableCopies();

        Copy copyToRent = availableCopies.stream()
                .filter((Copy copy) -> copy.getMovie().getId() == movieId).findFirst()
                .orElseThrow(() -> new NoAvailableCopyException("There are no available copies!"));

        return this.rentedCopyService.rentCopyOfAMovie(copyToRent).getCopy();
    }

    /**
     * Return a rented copy
     *
     * @param copyId The id of the copy to be returned
     */
    public Copy returnCopy(Integer copyId) {
        return this.rentedCopyService.setReturnedStatus(copyId).getCopy();
    }
}
