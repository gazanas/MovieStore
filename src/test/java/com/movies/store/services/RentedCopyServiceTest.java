package com.movies.store.services;

import com.movies.store.exceptions.CopyNotFoundException;
import com.movies.store.models.Copy;
import com.movies.store.models.RentedCopy;
import com.movies.store.models.User;
import com.movies.store.repositories.RentedCopyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "command.line.runner.enabled=false"})
@ExtendWith(MockitoExtension.class)
class RentedCopyServiceTest {

    private RentedCopyService rentedCopyService;

    @Mock
    private RentedCopyRepository rentedCopyRepository;

    @Mock
    private UserService userService;

    @Mock
    private ChargeService chargeService;



    public Copy mockedAvailableCopy;

    public Copy mockedUnavailableCopy;

    public RentedCopy mockedRentedCopy;

    public Copy mockedReturnedAvailableCopy;

    public RentedCopy mockedReturnedRentedCopy;

    public User mockedUser;

    @BeforeEach
    public void setUp() {
        rentedCopyService = new RentedCopyService(rentedCopyRepository, userService, chargeService);

        mockedUser = new User();
        mockedUser.setUsername("test");

        mockedUnavailableCopy = new Copy();
        mockedUnavailableCopy.setId(3);

        mockedRentedCopy = new RentedCopy();
        mockedRentedCopy.setId(1);
        mockedRentedCopy.setRentedAt(Timestamp.from(LocalDateTime.now().minus(2, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toInstant()));
        mockedRentedCopy.setCopy(mockedUnavailableCopy);
        mockedUser.setRentedCopies(List.of(mockedRentedCopy));

        mockedAvailableCopy = new Copy();
        mockedAvailableCopy.setId(1);

        mockedReturnedAvailableCopy = new Copy();
        mockedReturnedAvailableCopy.setId(2);

        mockedReturnedRentedCopy = new RentedCopy();
        mockedReturnedRentedCopy.setId(2);
        mockedReturnedRentedCopy.setCopy(mockedReturnedAvailableCopy);
        mockedRentedCopy.setRentedAt(Timestamp.from(LocalDateTime.now().minus(4, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toInstant()));
        mockedRentedCopy.setRentedAt(Timestamp.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetRentedCopyWhenCopyIsCurrentlyRented() {
        // given
        Integer copyId = 1;
        when(rentedCopyRepository.findByCopyIdAndReturnedAtIsNull(copyId))
                .thenReturn(Optional.ofNullable(mockedRentedCopy));

        // when
        RentedCopy rentedCopy = rentedCopyService.getRentedCopy(copyId);

        // then
        verify(rentedCopyRepository).findByCopyIdAndReturnedAtIsNull(copyId);
        assertEquals(1, rentedCopy.getId());
    }

    @Test
    public void testGetRentedCopyWhenCopyHasNeverBeenRented() {
        // given
        Integer copyId = 1;
        when(rentedCopyRepository.findByCopyIdAndReturnedAtIsNull(copyId)).thenReturn(Optional.ofNullable(null));

        // when
        RentedCopy rentedCopy = rentedCopyService.getRentedCopy(copyId);

        // then
        verify(rentedCopyRepository).findByCopyIdAndReturnedAtIsNull(copyId);
        assertEquals(null, rentedCopy);
    }

    @Test
    public void testGetRentedCopyWhenCopyHasBeenRentedAndReturned() {
        // given
        Integer copyId = 2;
        when(rentedCopyRepository.findByCopyIdAndReturnedAtIsNull(copyId))
                .thenReturn(Optional.ofNullable(mockedReturnedRentedCopy));

        // when
        RentedCopy rentedCopy = rentedCopyService.getRentedCopy(copyId);

        // then
        verify(rentedCopyRepository).findByCopyIdAndReturnedAtIsNull(copyId);
        assertEquals(2, rentedCopy.getId());
    }

    @Test
    @WithMockUser(username = "test")
    public void testRentCopyOfAMovieWhenCopyIsAvailable() {
        // given
        Copy copyToRent = mockedAvailableCopy;
        when(userService.getUserByUsername("test")).thenReturn(mockedUser);
        when(rentedCopyRepository.save(isA(RentedCopy.class))).thenAnswer(answer -> answer.getArgument(0));

        // when
        RentedCopy rentedCopy = rentedCopyService.rentCopyOfAMovie(copyToRent);

        // then
        verify(rentedCopyRepository).save(isA(RentedCopy.class));
        assertEquals(1, rentedCopy.getCopy().getId());
    }

    @Test
    @WithMockUser(username = "test")
    public void testReturnCopyOfAMovieWhenCopyIsRented() {
        // given
        Integer copyId = 3;
        when(rentedCopyRepository.findByCopyIdAndReturnedAtIsNull(copyId)).thenReturn(
                Optional.ofNullable(mockedRentedCopy));
        when(rentedCopyRepository.save(isA(RentedCopy.class))).thenAnswer(answer -> answer.getArgument(0));

        // when
        RentedCopy rentedCopy = rentedCopyService.setReturnedStatus(copyId);

        // then
        verify(rentedCopyRepository).save(isA(RentedCopy.class));
        assertNotNull(rentedCopy.getReturnedAt());
    }

    @Test
    @WithMockUser(username = "test")
    public void testReturnCopyOfAMovieWhenCopyIsNotRented() {
        // given
        Integer copyId = 1;
        when(rentedCopyRepository.findByCopyIdAndReturnedAtIsNull(copyId)).thenThrow(CopyNotFoundException.class);

        // then
        assertThrows(CopyNotFoundException.class, () -> rentedCopyService.setReturnedStatus(copyId));
    }

    @Test
    @WithMockUser(username = "test")
    public void testGetUsersRentedCopies() {
        // given
        when(userService.getUserByUsername("test")).thenReturn(mockedUser);

        // when
        List<RentedCopy> rentedCopies = rentedCopyService.getUsersRentedMovies();

        // then
        assertEquals(1, rentedCopies.size());
    }

    @Test
    public void testGetUsersRentedCopiesWhenUserIsNotAuthenticated() {
        // given
        // when

        // then
        assertThrows(AccessDeniedException.class, () -> rentedCopyService.getUsersRentedMovies());
    }
}