package com.movies.store.services;

import com.movies.store.dtos.ChargeDto;
import com.movies.store.mappers.ChargeMapper;
import com.movies.store.models.Charge;
import com.movies.store.models.RentedCopy;
import com.movies.store.repositories.ChargeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "command.line.runner.enabled=false"})
@ExtendWith(MockitoExtension.class)
class ChargeServiceTest {

    private ChargeService chargeService;

    @Mock
    private ChargeRepository chargeRepository;

    @Autowired
    private ChargeMapper chargeMapper;



    public Charge mockedCharge;

    public RentedCopy mockedReturnedCopy;

    @BeforeEach
    public void setUp() {
        chargeService = new ChargeService(chargeRepository, chargeMapper);

        mockedCharge = new Charge();
        mockedCharge.setId(1);
        mockedCharge.setAmount(1.0);
        mockedCharge.setPaid(false);
        mockedCharge.setRentedCopy(mock(RentedCopy.class));

        mockedReturnedCopy = new RentedCopy();
        mockedReturnedCopy.setId(1);
        mockedReturnedCopy.setRentedAt(Timestamp.from(LocalDateTime.now().minus(2, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toInstant()));
        mockedReturnedCopy.setReturnedAt(Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    @WithMockUser(username = "test")
    public void testGetUsersCharges() {
        // given
        when(chargeRepository.findByRentedCopyUserUsername("test"))
                .thenReturn(List.of((mockedCharge)));

        // when
        List<ChargeDto> charges = chargeService.getUsersCharges();

        // then
        assertEquals(1, charges.size());
        assertEquals(1.0, charges.get(0).getAmount());
    }

    @Test
    @WithMockUser(username = "test")
    public void testAddChargeForReturnedCopy() {
        // given
        when(chargeRepository.save(isA(Charge.class))).thenAnswer(answer -> answer.getArgument(0));

        // when
        Charge charge = chargeService.addChargeForReturnedCopy(mockedReturnedCopy);

        // then
        verify(chargeRepository).save(isA(Charge.class));
        assertEquals(2.0, charge.getAmount());
    }
}