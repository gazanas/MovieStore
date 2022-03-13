package com.movies.store.dtos;

import com.movies.store.validators.PasswordFormat;
import com.movies.store.validators.PasswordMatch;
import com.movies.store.validators.UniqueEmail;
import com.movies.store.validators.UniqueUsername;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@PasswordMatch(field = "password", match = "confirmation")
public class RegisterDto {

    @UniqueUsername
    private String username;

    @Email
    @UniqueEmail
    private String email;

    @PasswordFormat
    private String password;

    private String confirmation;
}
