package com.movies.store.controllers;

import com.movies.store.dtos.CopyDto;
import com.movies.store.services.CopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/copies")
@RequiredArgsConstructor
public class CopyController {

    private final CopyService copyService;

    @GetMapping(path = "/rented")
    public List<CopyDto> getRentedMovies() {
        return this.copyService.getUsersRentedCopies();
    }

    @PostMapping(path = "/movie/{movie_id}/rent")
    public ResponseEntity<String> rentMovie(@PathVariable(name = "movie_id") Integer id) {
        this.copyService.rentCopy(id);

        return ResponseEntity.ok().body("Movie added to your library!");
    }

    @PreAuthorize("@ownershipFilter.checkIfCopyIsRentedByCurrentUser(#copyId)")
    @PutMapping(path = "/{copy_id}/return")
    public ResponseEntity<String> returnMovie(@PathVariable(name = "copy_id") Integer copyId) {
        this.copyService.returnCopy(copyId);

        return ResponseEntity.ok().body("Movie returned!");
    }
}
