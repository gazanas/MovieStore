package com.movies.store;

import com.movies.store.dtos.RegisterDto;
import com.movies.store.models.*;
import com.movies.store.repositories.*;
import com.movies.store.services.RentedCopyService;
import com.movies.store.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.util.List;

@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}


	/**
	 * Initialize some data when the application is first run
	 * (First run means the database doesn't have data in the respective tables)
	 *
	 * @param categoryRepository
	 * @param movieRepository
	 * @param informationRepository
	 * @param actorRepository
	 * @param directorRepository
	 * @param copyRepository
	 * @return
	 */
	@ConditionalOnProperty(
			prefix = "command.line.runner",
			value = "enabled",
			havingValue = "true",
			matchIfMissing = true)
	@Bean
	public CommandLineRunner loadData(CategoryRepository categoryRepository,
									  MovieRepository movieRepository,
									  InformationRepository informationRepository,
									  ActorRepository actorRepository,
									  DirectorRepository directorRepository,
									  CopyRepository copyRepository) {

		return (args) -> {

			Category category1 = categoryRepository.findById(1).orElseGet(() -> {
				Category category = new Category();
				category.setCategory("Adventure");
				return categoryRepository.save(category);
			});

			Category category2 = categoryRepository.findById(2).orElseGet(() -> {
				Category category = new Category();
				category.setCategory("Comedy");
				return categoryRepository.save(category);
			});

			Movie movie1 = movieRepository.findById(1).orElseGet(() -> {
				Movie movie = new Movie();
				movie.setTitle("Lord Of The Rings The Fellowship of the Ring");
				movie.setCategory(category1);
				return movieRepository.save(movie);
			});

			Movie movie2 = movieRepository.findById(2).orElseGet(() -> {
				Movie movie = new Movie();
				movie.setTitle("Pirates of the Caribbean: The curse of the Black Pearl");
				movie.setCategory(category1);
				return movieRepository.save(movie);
			});

			Movie movie3 = movieRepository.findById(3).orElseGet(() -> {
				Movie movie = new Movie();
				movie.setTitle("Dracula Dead and Loving It");
				movie.setCategory(category2);
				return movieRepository.save(movie);
			});

			Information information1 = informationRepository.findByMovieId(movie1.getId()).orElseGet(() -> {
				Information information = new Information();
				information.setReleased(Timestamp.valueOf("2001-12-19 00:00:00"));
				information.setRating(8.9);
				information.setMovie(movie1);
				return informationRepository.save(information);
			});

			Information information2 = informationRepository.findByMovieId(movie2.getId()).orElseGet(() -> {
				Information information = new Information();
				information.setReleased(Timestamp.valueOf("2003-09-12 00:00:00"));
				information.setRating(8.1);
				information.setMovie(movie2);
				return informationRepository.save(information);
			});

			Information information3 = informationRepository.findByMovieId(movie3.getId()).orElseGet(() -> {
				Information information = new Information();
				information.setReleased(Timestamp.valueOf("1995-12-22 00:00:00"));
				information.setRating(5.8);
				information.setMovie(movie3);
				return informationRepository.save(information);
			});

			Actor actor1 = actorRepository.findById(1).orElseGet(() -> {
				Actor actor = new Actor();
				actor.setFirstName("Viggo");
				actor.setLastName("Mortensen");
				actor.setInformation(List.of(information1));
				return actorRepository.save(actor);
			});

			Actor actor2 = actorRepository.findById(2).orElseGet(() -> {
				Actor actor = new Actor();
				actor.setFirstName("Orlando");
				actor.setLastName("Bloom");
				actor.setInformation(List.of(information1, information2));
				return actorRepository.save(actor);
			});

			Actor actor3 = actorRepository.findById(3).orElseGet(() -> {
				Actor actor = new Actor();
				actor.setFirstName("Leslie");
				actor.setLastName("Nielsen");
				actor.setInformation(List.of(information3));
				return actorRepository.save(actor);
			});

			Copy copy1 = new Copy();
			copy1.setMovie(movie1);

			Copy copy2 = new Copy();
			copy2.setMovie(movie1);

			Copy copy3 = new Copy();
			copy3.setMovie(movie2);

			Copy copy4 = new Copy();
			copy4.setMovie(movie3);

			if (copyRepository.findAll().isEmpty()) {
				copyRepository.save(copy1);
				copyRepository.save(copy2);
				copyRepository.save(copy3);
				copyRepository.save(copy4);
			}
		};
	}
}
