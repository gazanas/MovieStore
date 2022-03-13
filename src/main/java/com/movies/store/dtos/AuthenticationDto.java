package com.movies.store.dtos;

import lombok.Data;

@Data
public class AuthenticationDto {

    private String username;

    private String password;
}
