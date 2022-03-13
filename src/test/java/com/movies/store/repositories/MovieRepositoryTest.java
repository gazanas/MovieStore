package com.movies.store.repositories;

import com.movies.store.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = {
        "command.line.runner.enabled=false"})
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CopyRepository copyRepository;

    @Autowired
    private RentedCopyRepository rentedCopyRepository;

    @Autowired
    private InformationRepository informationRepository;

    @Autowired
    private ActorRepository actorRepository;

    @BeforeEach
    public void setUp() {
        Category firstCategory = new Category();
        firstCategory.setCategory("Action");

        Category secondCategory = new Category();
        secondCategory.setCategory("Comedy");

        Category savedFirstCategory = categoryRepository.save(firstCategory);
        Category savedSecondCategory = categoryRepository.save(secondCategory);

        Movie firstMovie = new Movie();
        firstMovie.setTitle("John Wick");
        firstMovie.setCategory(savedFirstCategory);

        Movie secondMovie = new Movie();
        secondMovie.setTitle("Die Hard");
        secondMovie.setCategory(savedFirstCategory);

        Movie thirdMovie = new Movie();
        thirdMovie.setTitle("Dracula Dead and Loving It");
        thirdMovie.setCategory(savedSecondCategory);

        Movie fourthMovie = new Movie();
        fourthMovie.setTitle("Mission Impossible");
        fourthMovie.setCategory(savedFirstCategory);

        Movie fifthMovie = new Movie();
        fifthMovie.setTitle("James Bond Die Another Day");
        fifthMovie.setCategory(savedFirstCategory);

        Movie sixthMovie = new Movie();
        sixthMovie.setTitle("Matrix Reloaded");
        sixthMovie.setCategory(firstCategory);

        Movie savedFirstMovie = movieRepository.save(firstMovie);
        Movie savedSecondMovie = movieRepository.save(secondMovie);
        Movie savedThirdMovie = movieRepository.save(thirdMovie);
        Movie savedFourthMovie = movieRepository.save(fourthMovie);
        Movie savedFifthMovie = movieRepository.save(fifthMovie);
        Movie savedSixthMovie = movieRepository.save(sixthMovie);

        Information firstInformation = new Information();
        firstInformation.setMovie(firstMovie);
        firstInformation.setRating(4.0);
        firstInformation.setReleased(Timestamp.valueOf("2015-01-01 00:00:00"));

        Information secondInformation = new Information();
        secondInformation.setMovie(sixthMovie);
        secondInformation.setRating(2.0);
        secondInformation.setReleased(Timestamp.valueOf("2009-01-01 00:00:00"));

        Information thirdInformation = new Information();
        thirdInformation.setMovie(secondMovie);
        thirdInformation.setRating(3.5);
        thirdInformation.setReleased(Timestamp.valueOf("2000-01-01 00:00:00"));

        Information savedFirstInformation = informationRepository.save(firstInformation);
        Information savedSecondInformation = informationRepository.save(secondInformation);
        Information savedThirdInformation = informationRepository.save(thirdInformation);

        Actor firstActor = new Actor();
        firstActor.setFirstName("Keeanu");
        firstActor.setLastName("Reeves");
        firstActor.setInformation(List.of(savedFirstInformation, savedSecondInformation));

        Actor secondActor = new Actor();
        secondActor.setFirstName("Bruce");
        secondActor.setLastName("Willis");
        secondActor.setInformation(List.of(savedThirdInformation));

        Actor savedFirstActor = actorRepository.save(firstActor);
        Actor savedSecondActor = actorRepository.save(secondActor);

        Copy firstCopy = new Copy();
        firstCopy.setMovie(savedFirstMovie);

        Copy secondCopy = new Copy();
        secondCopy.setMovie(savedFirstMovie);

        Copy thirdCopy = new Copy();
        thirdCopy.setMovie(savedSecondMovie);

        Copy fourthCopy = new Copy();
        fourthCopy.setMovie(savedThirdMovie);

        Copy fifthCopy = new Copy();
        fifthCopy.setMovie(savedThirdMovie);

        Copy savedFirstCopy = copyRepository.save(firstCopy);
        Copy savedSecondCopy = copyRepository.save(secondCopy);
        Copy savedThirdCopy = copyRepository.save(thirdCopy);
        Copy savedFourthCopy = copyRepository.save(fourthCopy);
        Copy savedFifthCopy = copyRepository.save(fifthCopy);

        RentedCopy firstRented = new RentedCopy();
        firstRented.setRentedAt(Timestamp.from(LocalDateTime.now().minus(10, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toInstant()));
        firstRented.setCopy(savedFirstCopy);

        RentedCopy secondRented = new RentedCopy();
        firstRented.setRentedAt(Timestamp.from(LocalDateTime.now().minus(2, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toInstant()));
        firstRented.setCopy(savedThirdCopy);

        RentedCopy thirdRented = new RentedCopy();
        firstRented.setRentedAt(Timestamp.from(LocalDateTime.now().minus(3, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toInstant()));
        firstRented.setCopy(savedThirdCopy);

        rentedCopyRepository.save(firstRented);
        rentedCopyRepository.save(secondRented);
        rentedCopyRepository.save(thirdRented);
    }

    @AfterEach
    public void tearDown() {
        categoryRepository.deleteAll();
        movieRepository.deleteAll();
        copyRepository.deleteAll();
        rentedCopyRepository.deleteAll();
    }

    @Test
    public void searchByCategory() {
        // given
        String category = "Action";

        // when
        List<Movie> movies = movieRepository.searchMovie(null, category, null, null, null, null);

        // then
        assertEquals(5, movies.size());
    }

    @Test
    public void searchByTitle() {
        // given
        String title = "Die";

        // when
        List<Movie> movies = movieRepository.searchMovie(title, null, null, null, null, null);

        // then
        assertEquals(2, movies.size());
    }

    @Test
    public void searchByReleasedDate() {
        // given
        Timestamp date = Timestamp.valueOf("2015-01-01 00:00:00");

        // when
        List<Movie> movies = movieRepository.searchMovie(null, null, date, null, null, null);

        // then
        assertEquals(1, movies.size());
    }

    @Test
    public void searchByTitleAndRating() {
        // given
        String title = "Die";
        Double rating = 4.0;

        // when
        List<Movie> movies = movieRepository.searchMovie(title, null, null, rating, null, null);

        // then
        assertEquals(0, movies.size());

        // given
        title = "Wick";

        // when
        movies = movieRepository.searchMovie(title, null, null, rating, null, null);

        // then
        assertEquals(1, movies.size());
    }

    @Test
    public void searchByActors() {
        // given
        List<Actor> actors = actorRepository.findByFirstNameOrLastName("Keeanu");

        // when
        List<Movie> movies = movieRepository.searchMovie(null, null, null, null, actors, null);

        // then
        assertEquals(2, movies.size());

        // given
        actors = Stream.concat(actorRepository.findByFirstNameOrLastName("Keeanu").stream(),
                actorRepository.findByFirstNameOrLastName("Willis").stream()).collect(Collectors.toList());

        // when
        movies = movieRepository.searchMovie(null, null, null, null, actors, null);


        // then
        assertEquals(3, movies.size());
    }
}