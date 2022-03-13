package com.movies.store.services;

import com.movies.store.dtos.MovieDto;
import com.movies.store.mappers.MovieMapper;
import com.movies.store.models.Actor;
import com.movies.store.models.Copy;
import com.movies.store.models.Information;
import com.movies.store.models.Movie;
import com.movies.store.repositories.ActorRepository;
import com.movies.store.repositories.DirectorRepository;
import com.movies.store.repositories.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "command.line.runner.enabled=false"})
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private CopyService copyService;

    @Mock
    ActorRepository actorRepository;

    @Mock
    DirectorRepository directorRepository;

    @Autowired
    MovieMapper movieMapper;

    public List<Movie> mockedMovies;

    public List<Copy> mockedAvailableCopies;

    @BeforeEach
    public void setUp() {

        movieService = new MovieService(movieRepository, copyService, actorRepository, directorRepository, movieMapper);

        mockedMovies = new ArrayList<>();
        mockedAvailableCopies = new ArrayList<>();

        Actor actor1 = new Actor();
        actor1.setId(1);
        actor1.setFirstName("Keeanu");
        actor1.setLastName("Reeves");

        Actor actor2 = new Actor();
        actor2.setId(2);
        actor2.setFirstName("Vigo");
        actor2.setLastName("Mortesen");

        Information information1 = new Information();
        information1.setId(1);
        information1.setRating(4.0);
        information1.setReleased(Timestamp.valueOf("2000-01-01 00:00:00"));
        information1.setActors(List.of(actor1));

        Information information2 = new Information();
        information2.setId(2);
        information2.setRating(5.0);
        information2.setReleased(Timestamp.valueOf("2009-01-01 00:00:00"));
        information2.setActors(List.of(actor1, actor2));

        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setInformation(information1);

        Movie movie2 = new Movie();
        movie2.setId(2);
        movie2.setInformation(information2);

        mockedMovies.add(movie1);
        mockedMovies.add(movie2);

        Copy copy1 = new Copy();
        copy1.setId(1);
        copy1.setMovie(movie1);

        Copy copy2 = new Copy();
        copy2.setId(2);
        copy2.setMovie(movie2);

        Copy copy3 = new Copy();
        copy3.setId(3);
        copy3.setMovie(movie1);

        mockedAvailableCopies.add(copy1);
        mockedAvailableCopies.add(copy2);
        mockedAvailableCopies.add(copy3);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testListAvailableMovies() {
        // given
        when(copyService.getAllAvailableCopies()).thenReturn(mockedAvailableCopies);

        // when
        List<MovieDto> movies = movieService.listAvailableMovies();

        // then
        assertEquals(2, movies.size());
    }

    @Test
    public void testSearchByTitleWhenThereAreAvailableCopies() {
        // given
        String titleTerm = "Hello";
        when(copyService.getAllAvailableCopies()).thenReturn(mockedAvailableCopies);
        when(movieRepository.searchMovie(titleTerm, null, null, null, null, null))
                .thenReturn(mockedMovies);

        // when
        List<MovieDto> movies = movieService.searchAvailableMovies(titleTerm, null, null, null, null, null);

        // then
        assertEquals(2, movies.size());
    }

    @Test
    public void testSearchByTitleWhenThereAreNoAvailableCopies() {
        // given
        String titleTerm = "Matrix";
        when(copyService.getAllAvailableCopies()).thenReturn(List.of());
        when(movieRepository.searchMovie(titleTerm, null, null, null, null, null))
                .thenReturn(mockedMovies);

        // when
        List<MovieDto> movies = movieService.searchAvailableMovies(titleTerm, null, null, null, null, null);

        // then
        assertEquals(0, movies.size());
    }
}