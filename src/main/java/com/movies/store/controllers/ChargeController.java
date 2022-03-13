package com.movies.store.controllers;

import com.movies.store.dtos.ChargeDto;
import com.movies.store.services.ChargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/charges")
@RequiredArgsConstructor
public class ChargeController {

    private final ChargeService chargeService;

    @GetMapping
    public List<ChargeDto> getUsersCharges() {
        return this.chargeService.getUsersCharges();
    }

}
