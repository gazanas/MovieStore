package com.movies.store.controllers;

import com.movies.store.dtos.InformationDto;
import com.movies.store.dtos.MovieDto;
import com.movies.store.services.InformationService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/info/movie")
@RequiredArgsConstructor
public class InformationController {

    private final InformationService informationService;

    @ApiResponse(description = "Returns the information about a movie if it exists", responseCode = "200",
            content = @Content(schema = @Schema(implementation = InformationDto.class),
                    examples = @ExampleObject(value =
                    "{\n" +
                            "  \"id\": 1,\n" +
                            "  \"summary\": \"A meek Hobbit from the Shire and eight companions set out on a journey to " +
                            "destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.\",\n" +
                            "  \"released\": \"2001-12-19 00:00:00Z\",\n" +
                            "  \"rating\": 8.9,\n" +
                            "  \"actors\": [\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"firstName\": \"Viggo\",\n" +
                            "      \"lastName\": \"Mortensen\",\n" +
                            "      \"createdAt\": \"2022-03-14T09:23:50.926Z\",\n" +
                            "      \"updatedAt\": \"2022-03-14T09:23:50.926Z\"\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"directors\": [\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"firstName\": \"Peter\",\n" +
                            "      \"lastName\": \"Jackson\",\n" +
                            "      \"createdAt\": \"2022-03-14T09:23:50.926Z\",\n" +
                            "      \"updatedAt\": \"2022-03-14T09:23:50.926Z\"\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"createdAt\": \"2022-03-14T09:23:50.926Z\",\n" +
                            "  \"updatedAt\": \"2022-03-14T09:23:50.926Z\"\n" +
                            "}")))
    @GetMapping(path = "/{movie_id}")
    public InformationDto movieInformation(@PathVariable(name = "movie_id") Integer movieId) {
        return informationService.getMovieInformation(movieId);
    }
}
