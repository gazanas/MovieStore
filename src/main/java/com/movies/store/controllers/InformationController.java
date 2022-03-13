package com.movies.store.controllers;

import com.movies.store.dtos.InformationOutDto;
import com.movies.store.services.InformationService;
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

    @GetMapping(path = "/{movie_id}")
    public InformationOutDto movieInformation(@PathVariable(name = "movie_id") Integer movieId) {
        return informationService.getMovieInformation(movieId);
    }
}
