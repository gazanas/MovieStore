package com.movies.store.controllers;

import com.movies.store.dtos.MovieDto;
import com.movies.store.services.MovieService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @ApiResponse(description = "Returns a list of all the available movies", responseCode = "200",
            content = @Content(schema = @Schema(implementation = MovieDto.class),
                    examples = @ExampleObject(value =
                            "[\n" +
                                    "  {\n" +
                                    "    \"id\": 1,\n" +
                                    "    \"title\": \"Die Hard\",\n" +
                                    "    \"categoryId\": 1,\n" +
                                    "    \"createdAt\": \"2022-03-01T08:02:22.348Z\",\n" +
                                    "    \"updatedAt\": \"2022-03-01T08:02:22.348Z\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"id\": 14,\n" +
                                    "    \"title\": \"James Bond Die Another Day\",\n" +
                                    "    \"categoryId\": 1,\n" +
                                    "    \"createdAt\": \"2022-03-14T08:02:22.348Z\",\n" +
                                    "    \"updatedAt\": \"2022-03-14T08:02:22.348Z\"\n" +
                                    "  }\n" +
                                    "]")))
    @GetMapping(path = "/available")
    public List<MovieDto> availableMovies() {
        return movieService.listAvailableMovies();
    }

    @ApiResponse(description = "Searches the available movies", responseCode = "200",
            content = @Content(schema = @Schema(implementation = MovieDto.class),
                    examples = @ExampleObject(value =
                    "[\n" +
                            "  {\n" +
                            "    \"id\": 1,\n" +
                            "    \"title\": \"Die Hard\",\n" +
                            "    \"categoryId\": 1,\n" +
                            "    \"createdAt\": \"2022-03-01T08:02:22.348Z\",\n" +
                            "    \"updatedAt\": \"2022-03-01T08:02:22.348Z\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"id\": 14,\n" +
                            "    \"title\": \"James Bond Die Another Day\",\n" +
                            "    \"categoryId\": 1,\n" +
                            "    \"createdAt\": \"2022-03-14T08:02:22.348Z\",\n" +
                            "    \"updatedAt\": \"2022-03-14T08:02:22.348Z\"\n" +
                            "  }\n" +
                            "]")))
    @GetMapping(path = "/search")
    public List<MovieDto> searchMovies(@RequestParam(name = "title", required = false) String title,
                                       @RequestParam(name = "category", required = false) String category,
                                       @RequestParam(name = "released", required = false) String released,
                                       @RequestParam(name = "rating", required = false) Double rating,
                                       @RequestParam(name = "actors", required = false) List<String> actors,
                                       @RequestParam(name = "directors", required = false) List<String> directors) {
        return movieService.searchAvailableMovies(title, category, released, rating, actors, directors);
    }

    @ApiResponse(description = "If the current user has rented the requested movie then stream it", responseCode = "200",
            content = @Content(schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(value = "Movie is loading...")))
    @PreAuthorize("@ownershipFilter.checkIfMovieIsRentedByCurrentUser(#id)")
    @GetMapping(path = "/movie/{movie_id}/play")
    public ResponseEntity<String> playMovie(@PathVariable(name = "movie_id") Integer id) {
        return ResponseEntity.ok().body("Movie is loading...!");
    }

    @ApiResponse(description = "Searches for an available copy of the requested movie and if it finds one it rents it",
            responseCode = "200", content = @Content(schema = @Schema(implementation = String.class),
            examples = @ExampleObject(value = "Movie added to your library!")))
    @PostMapping(path = "/movie/{movie_id}/rent")
    public ResponseEntity<String> rentMovie(@PathVariable(name = "movie_id") Integer id) {
        this.movieService.rentMovie(id);

        return ResponseEntity.ok().body("Movie added to your library!");
    }
}
