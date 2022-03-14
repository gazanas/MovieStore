package com.movies.store.controllers;

import com.movies.store.dtos.CopyDto;
import com.movies.store.services.CopyService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @ApiResponse(description = "Returns a list of copies that the user has rented  (authenticated)", responseCode = "200",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CopyDto.class)),
                    examples = @ExampleObject(value =
                            "[\n" +
                            "  {\n" +
                            "    \"id\": 1,\n" +
                            "    \"movieId\": 2,\n" +
                            "    \"createdAt\": \"2022-03-14T09:01:58.228Z\",\n" +
                            "    \"updatedAt\": \"2022-03-14T09:01:58.228Z\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"id\": 2,\n" +
                            "    \"movieId\": 5,\n" +
                            "    \"createdAt\": \"2022-03-14T09:01:58.228Z\",\n" +
                            "    \"updatedAt\": \"2022-03-14T09:01:58.228Z\"\n" +
                            "  },\n" +
                            "]")))
    @GetMapping(path = "/rented")
    public List<CopyDto> getRentedMovies() {
        return this.copyService.getUsersRentedCopies();
    }

    @ApiResponse(description = "If the current user has rented the requested copy then it returns it  (authenticated)", responseCode = "200",
            content = @Content(schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(value = "Movie returned!")))
    @PreAuthorize("@ownershipFilter.checkIfCopyIsRentedByCurrentUser(#copyId)")
    @PutMapping(path = "/{copy_id}/return")
    public ResponseEntity<String> returnMovie(@PathVariable(name = "copy_id") Integer copyId) {
        this.copyService.returnCopy(copyId);

        return ResponseEntity.ok().body("Movie returned!");
    }
}
