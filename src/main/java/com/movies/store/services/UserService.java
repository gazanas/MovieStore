package com.movies.store.services;

import com.movies.store.dtos.RegisterDto;
import com.movies.store.models.User;
import com.movies.store.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Get a user entity by their username
     *
     * @param username The requested username
     * @return The resulted User entity
     */
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username could not be found!"));
    }

    /**
     * Add a new user
     *
     * @param userDto The user input dto
     * @return The resulted user entity after adding the new user
     */
    public User addUser(RegisterDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

        return this.userRepository.save(user);
    }
}
