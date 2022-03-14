package com.movies.store.controllers;

import com.movies.store.dtos.ChargeDto;
import com.movies.store.services.ChargeService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @ApiResponse(description = "A list of charges for the unpaid returned movies of the current user", responseCode = "200",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChargeDto.class)),
                    examples = @ExampleObject(value =
                    "[\n" +
                            "  {\n" +
                            "    \"id\": 1,\n" +
                            "    \"amount\": 2,\n" +
                            "    \"paid\": false,\n" +
                            "    \"copyId\": 1,\n" +
                            "    \"createdAt\": \"2022-03-14T08:52:36.729Z\",\n" +
                            "    \"updatedAt\": \"2022-03-14T08:52:36.729Z\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"id\": 2,\n" +
                            "    \"amount\": 4.5,\n" +
                            "    \"paid\": false,\n" +
                            "    \"copyId\": 2,\n" +
                            "    \"createdAt\": \"2022-03-14T08:52:36.729Z\",\n" +
                            "    \"updatedAt\": \"2022-03-14T08:52:36.729Z\"\n" +
                            "  }\n" +
                            "]")))
    @GetMapping
    public List<ChargeDto> getUsersCharges() {
        return this.chargeService.getUsersCharges();
    }

}
