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

@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}

	@ConditionalOnProperty(
			prefix = "command.line.runner",
			value = "enabled",
			havingValue = "true",
			matchIfMissing = true)
	@Bean
	public CommandLineRunner loadData(CategoryRepository categoryRepository,
									  MovieRepository movieRepository,
									  InformationRepository informationRepository,
									  CopyRepository copyRepository) {

		return (args) -> {

			Category category1 = new Category();
			category1.setCategory("Adventure");

			Category category2 = new Category();
			category2.setCategory("Comedy");

			if (categoryRepository.findAll().isEmpty()) {
				categoryRepository.save(category1);
				categoryRepository.save(category2);
			}

			Movie movie1 = new Movie();
			movie1.setTitle("Lord Of The Rings The Fellowship of the Ring");
			movie1.setCategory(category1);

			Movie movie2 = new Movie();
			movie2.setTitle("Dracula Dead and Loving It");
			movie2.setCategory(category2);

			if (movieRepository.findAll().isEmpty()) {
				movieRepository.save(movie1);
				movieRepository.save(movie2);
			}

			Information information1 = new Information();
			information1.setReleased(Timestamp.valueOf("2001-12-19 00:00:00"));
			information1.setRating(8.9);
			information1.setMovie(movie1);

			Information information2 = new Information();
			information2.setReleased(Timestamp.valueOf("1995-12-22 00:00:00"));
			information2.setRating(5.8);
			information2.setMovie(movie2);

			if (informationRepository.findAll().isEmpty()) {
				informationRepository.save(information1);
				informationRepository.save(information2);
			}

			Copy copy1 = new Copy();
			copy1.setMovie(movie1);

			Copy copy2 = new Copy();
			copy2.setMovie(movie1);

			Copy copy3 = new Copy();
			copy3.setMovie(movie2);

			if (copyRepository.findAll().isEmpty()) {
				copyRepository.save(copy1);
				copyRepository.save(copy2);
				copyRepository.save(copy3);
			}
		};
	}
}
