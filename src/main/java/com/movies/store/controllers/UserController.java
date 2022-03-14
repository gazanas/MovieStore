package com.movies.store.controllers;

import com.movies.store.dtos.RegisterDto;
import com.movies.store.services.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiResponse(description = "Creates a new user", responseCode = "201")
    @PostMapping(path = "/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterDto userInDto) {
        this.userService.addUser(userInDto);
        return ResponseEntity.ok().body("User registered successfully!");
    }
}
