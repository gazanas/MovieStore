package com.movies.store.services;

import com.movies.store.dtos.InformationDto;
import com.movies.store.exceptions.InformationNotFoundException;
import com.movies.store.mappers.InformationMapper;
import com.movies.store.models.Information;
import com.movies.store.models.Movie;
import com.movies.store.repositories.InformationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "command.line.runner.enabled=false"})
@ExtendWith(MockitoExtension.class)
class InformationServiceTest {

    private InformationService informationService;

    @Mock
    private InformationRepository informationRepository;

    @Autowired
    private InformationMapper informationMapper;


    public List<Movie> mockedMovies;

    @BeforeEach
    public void setUp() {
        informationService = new InformationService(informationRepository, informationMapper);

        mockedMovies = new ArrayList<>();

        Information information1 = new Information();
        information1.setId(1);
        information1.setRating(4.0);

        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setInformation(information1);

        Movie movie2 = new Movie();
        movie2.setId(2);

        mockedMovies.add(movie1);
        mockedMovies.add(movie2);
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testGetMovieInformationWhenInformationExists() {
        // given
        Integer movieId = 1;
        when(informationRepository.findByMovieId(movieId))
                .thenReturn(Optional.ofNullable(mockedMovies.get(0).getInformation()));

        // when
        InformationDto information = informationService.getMovieInformation(movieId);

        // then
        verify(informationRepository).findByMovieId(movieId);
        assertEquals(1, information.getId());
    }

    @Test
    public void testGetMovieInformationWhenInformationDoesNotExist() {
        // given
        Integer movieId = 2;
        when(informationRepository.findByMovieId(movieId))
                .thenReturn(Optional.ofNullable(null));
        // when
        // then
        assertThrows(InformationNotFoundException.class, () -> informationService.getMovieInformation(movieId));
    }
}