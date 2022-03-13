package com.movies.store.services;

import com.movies.store.dtos.ChargeDto;
import com.movies.store.mappers.ChargeMapper;
import com.movies.store.models.Charge;
import com.movies.store.models.RentedCopy;
import com.movies.store.repositories.ChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChargeService {

    private final ChargeRepository chargeRepository;

    private final ChargeMapper chargeMapper;

    /**
     * Get the charge of the returned movie copies for the current user
     *
     * @return A list of all the charges of the rented copies of the current user
     */
    public List<ChargeDto> getUsersCharges() {
        return this.chargeRepository.findByRentedCopyUserUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName()).stream()
                .filter((Charge charge) -> charge.getPaid() == false).map(this.chargeMapper::chargeToDto)
                .collect(Collectors.toList());
    }

    /**
     * Create a new charge entry for the returned movie copy
     *
     * @param returnedCopy The returned movie copy
     * @return The charge entity for the returned movie copy
     */
    public Charge addChargeForReturnedCopy(RentedCopy returnedCopy) {
        Charge charge = new Charge();
        charge.setAmount(calculateChargeAmount(returnedCopy.getRentedAt(), returnedCopy.getReturnedAt()));
        charge.setPaid(false);
        charge.setRentedCopy(returnedCopy);
        return this.chargeRepository.save(charge);
    }

    /**
     * Calculate the amount charged for renting the movie, 1.0 for the first
     * three days and 0.5 for each next day
     *
     * @param rentedAt The date the movie copy was rented
     * @param returnedAt The date the movie copy was returned
     * @return The amount charged for the days the movie copy was rented
     */
    private Double calculateChargeAmount(Timestamp rentedAt, Timestamp returnedAt) {
        LocalDateTime start = rentedAt.toLocalDateTime();
        LocalDateTime end = returnedAt.toLocalDateTime();
        Long days = ChronoUnit.DAYS.between(start, end);

        Double amount = 1.0;

        for (Integer i = 0; i < days-1; i++) {
            if (i < 2) {
                amount += 1.0;
            } else {
                amount += 0.5;
            }
        }

        return amount;
    }
}
