package com.movies.store.controllers;

import com.movies.store.dtos.MovieDto;
import com.movies.store.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping(path = "/available")
    public List<MovieDto> availableMovies() {
        return movieService.listAvailableMovies();
    }

    @GetMapping(path = "/search")
    public List<MovieDto> searchMovies(@RequestParam(name = "title", required = false) String title,
                                       @RequestParam(name = "category", required = false) String category,
                                       @RequestParam(name = "released", required = false) String released,
                                       @RequestParam(name = "rating", required = false) Double rating,
                                       @RequestParam(name = "actors", required = false) List<String> actors,
                                       @RequestParam(name = "directors", required = false) List<String> directors) {
        return movieService.searchAvailableMovies(title, category, released, rating, actors, directors);
    }
}
