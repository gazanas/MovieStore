package com.movies.store.services;

import com.movies.store.dtos.InformationOutDto;
import com.movies.store.exceptions.InformationNotFoundException;
import com.movies.store.mappers.InformationMapper;
import com.movies.store.models.Information;
import com.movies.store.repositories.InformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final InformationRepository informationRepository;

    private final InformationMapper informationMapper;

    /**
     * Fetches the information about a movie and converts it to a dto
     * so that it can be sent into the http response body.
     *
     * If the movie is not found then it throws a Not Found exception
     *
     * @param movieId The id of the movie
     * @return The dto that will be sent in the response body
     */
    public InformationOutDto getMovieInformation(Integer movieId) {
        Information information = informationRepository.findByMovieId(movieId).orElseThrow(() ->
                new InformationNotFoundException("Could not find information about this movie!"));

        return informationMapper.informationToOutputDto(information);
    }
}
