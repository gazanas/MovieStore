package com.movies.store;

import com.movies.store.dtos.RegisterDto;
import com.movies.store.models.*;
import com.movies.store.repositories.CategoryRepository;
import com.movies.store.repositories.CopyRepository;
import com.movies.store.repositories.InformationRepository;
import com.movies.store.repositories.MovieRepository;
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
	public CommandLineRunner loadData(UserService userService,
									  CategoryRepository categoryRepository,
									  MovieRepository movieRepository,
									  InformationRepository informationRepository,
									  CopyRepository copyRepository) {

		return (args) -> {

			RegisterDto registerDto1 = new RegisterDto();
			registerDto1.setUsername("test1");
			registerDto1.setEmail("test1@test.test");
			registerDto1.setPassword("password123!@");
			registerDto1.setConfirmation("password123!@");

			RegisterDto registerDto2 = new RegisterDto();
			registerDto2.setUsername("test2");
			registerDto2.setEmail("test2@test.test");
			registerDto2.setPassword("password123!@");
			registerDto2.setConfirmation("password123!@");

			User user1 = userService.addUser(registerDto1);
			User user2 = userService.addUser(registerDto2);

			Category category1 = new Category();
			category1.setCategory("Adventure");
			categoryRepository.save(category1);

			Category category2 = new Category();
			category2.setCategory("Comedy");
			categoryRepository.save(category2);

			categoryRepository.save(category1);
			categoryRepository.save(category2);

			Movie movie1 = new Movie();
			movie1.setTitle("Lord Of The Rings The Fellowship of the Ring");
			movie1.setCategory(category1);

			Movie movie2 = new Movie();
			movie2.setTitle("Dracula Dead and Loving It");
			movie2.setCategory(category2);

			movieRepository.save(movie1);
			movieRepository.save(movie2);

			Information information1 = new Information();
			information1.setReleased(Timestamp.valueOf("2001-12-19 00:00:00"));
			information1.setRating(8.9);
			information1.setMovie(movie1);

			Information information2 = new Information();
			information2.setReleased(Timestamp.valueOf("1995-12-22 00:00:00"));
			information2.setRating(5.8);
			information2.setMovie(movie2);

			informationRepository.save(information1);
			informationRepository.save(information2);

			Copy copy1 = new Copy();
			copy1.setMovie(movie1);

			Copy copy2 = new Copy();
			copy2.setMovie(movie1);

			Copy copy3 = new Copy();
			copy3.setMovie(movie2);

			copyRepository.save(copy1);
			copyRepository.save(copy2);
			copyRepository.save(copy3);
		};
	}
}
