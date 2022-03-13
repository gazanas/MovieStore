package com.movies.store.services;

import com.movies.store.dtos.MovieDto;
import com.movies.store.mappers.MovieMapper;
import com.movies.store.models.*;
import com.movies.store.repositories.ActorRepository;
import com.movies.store.repositories.DirectorRepository;
import com.movies.store.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    private final CopyService copyService;

    private final ActorRepository actorRepository;

    private final DirectorRepository directorRepository;

    private final MovieMapper movieMapper;

    /**
     * Lists all available movies
     *
     * @return List of all available movies
     */
    public List<MovieDto> listAvailableMovies() {
        return this.copyService.getAllAvailableCopies().stream().map(Copy::getMovie)
                .map(this.movieMapper::movieToOutputDto).collect(collectingAndThen(toCollection(() ->
                                new TreeSet<>(comparingInt(MovieDto::getId))),
                        ArrayList::new));
    }

    /**
     * Searches available movies based on criteria
     *
     * @param title Title parameter
     * @param category Category parameter
     * @param released Released Date parameter
     * @param rating Rating parameter
     * @param actors List of actors parameter
     * @param directors List of directors parameter
     * @return List of movies resulted from the search criteria
     */
    public List<MovieDto> searchAvailableMovies(String title, String category, String released, Double rating,
                                                List<String> actors, List<String> directors) {
        Timestamp releasedDate = (released == null) ? null : Timestamp.valueOf(released);
        List<Actor> actorList = (actors == null) ? null : actors.stream().map(this.actorRepository::findByFirstNameOrLastName)
                .flatMap(List::stream).collect(Collectors.toList());
        List<Director> directorList = (directors == null) ? null : directors.stream().map(this.directorRepository::findByFirstNameOrLastName)
                .flatMap(List::stream).collect(Collectors.toList());
        return movieRepository.searchMovie(title, category, releasedDate, rating, actorList, directorList)
                .stream().map(this.movieMapper::movieToOutputDto)
                .filter((MovieDto movie) -> this.listAvailableMovies().contains(movie)).collect(Collectors.toList());
    }
}
