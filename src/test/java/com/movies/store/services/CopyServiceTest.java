package com.movies.store.services;

import com.movies.store.dtos.CopyDto;
import com.movies.store.exceptions.NoAvailableCopyException;
import com.movies.store.exceptions.ReturnCopyException;
import com.movies.store.mappers.CopyMapper;
import com.movies.store.models.Copy;
import com.movies.store.models.Movie;
import com.movies.store.models.RentedCopy;
import com.movies.store.models.User;
import com.movies.store.repositories.CopyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "command.line.runner.enabled=false"})
@ExtendWith(MockitoExtension.class)
class CopyServiceTest {

    private CopyService copyService;

    @Mock
    private CopyRepository copyRepository;

    @Mock
    private RentedCopyService rentedCopyService;

    @Autowired
    private CopyMapper copyMapper;

    public User mockedUser;

    public List<Copy> mockedCopies;

    public List<RentedCopy> mockedRentedCopies;

    @BeforeEach
    public void setUp() {
        copyService = new CopyService(copyRepository, rentedCopyService, copyMapper);

        mockedCopies = new ArrayList<>();
        mockedRentedCopies = new ArrayList<>();

        mockedUser = new User();
        mockedUser.setUsername("test");

        Movie movie1 = new Movie();
        movie1.setId(1);

        Movie movie2 = new Movie();
        movie2.setId(2);

        Copy copy1 = new Copy();
        copy1.setId(1);
        copy1.setMovie(movie1);

        Copy copy2 = new Copy();
        copy2.setId(2);
        copy2.setMovie(movie1);

        Copy copy3 = new Copy();
        copy3.setId(3);
        copy3.setMovie(movie2);

        mockedCopies.add(copy1);
        mockedCopies.add(copy2);
        mockedCopies.add(copy3);

        RentedCopy rentedCopy1 = new RentedCopy();
        rentedCopy1.setId(1);
        rentedCopy1.setCopy(copy3);
        rentedCopy1.setRentedAt(Timestamp.from(LocalDateTime.now().minus(2, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toInstant()));
        mockedUser.setRentedCopies(List.of(rentedCopy1));

        mockedRentedCopies.add(rentedCopy1);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void getAvailableCopies() {
        // given
        when(copyRepository.findAll()).thenReturn(mockedCopies);
        when(rentedCopyService.getRentedCopy(isA(Integer.class))).thenAnswer(answer -> mockedRentedCopies.stream()
                .filter(rc -> rc.getCopy().getId() == answer.getArgument(0)).findFirst().orElse(null));

        // when
        List<Copy> availableCopies = copyService.getAllAvailableCopies();

        // then
        verify(copyRepository).findAll();
        verify(rentedCopyService, times(3)).getRentedCopy(isA(Integer.class));
        assertEquals(2, availableCopies.size());
    }

    @Test
    @WithMockUser(username = "test")
    public void testGetRentedCopiesByCurrentUser() {
        // given
        when(rentedCopyService.getUsersRentedMovies()).thenReturn(mockedRentedCopies);

        // when
        List<CopyDto> rentedCopies = copyService.getUsersRentedCopies();

        // then
        verify(rentedCopyService).getUsersRentedMovies();
        assertEquals(1, rentedCopies.size());
        assertEquals(3, rentedCopies.get(0).getId());
    }

    @Test
    public void testGetRentedCopiesWhenUserIsNotAuthenticated() {
        // given
        when(rentedCopyService.getUsersRentedMovies())
                .thenThrow(AccessDeniedException.class);

        // when

        // then
        assertThrows(AccessDeniedException.class, () -> copyService.getUsersRentedCopies());
    }

    @Test
    @WithMockUser(username = "test")
    public void testRentMovieWhenCopyIsAvailable() {
        // given
        Integer movieId = 1;
        when(copyRepository.findAll()).thenReturn(mockedCopies);
        when(rentedCopyService.getRentedCopy(isA(Integer.class))).thenAnswer(answer -> mockedRentedCopies.stream()
                .filter(rc -> rc.getCopy().getId() == answer.getArgument(0)).findFirst().orElse(null));
        RentedCopy mockedRentedCopy = mock(RentedCopy.class);
        when(rentedCopyService.rentCopyOfAMovie(isA(Copy.class))).thenReturn(mockedRentedCopy);

        // when
        copyService.rentCopy(movieId);

        // then
        verify(rentedCopyService).rentCopyOfAMovie(isA(Copy.class));
    }

    @Test
    @WithMockUser(username = "test")
    public void testRentMovieWhenNoCopyIsAvailable() {
        // given
        Integer movieId = 1;

        // when
        when(copyRepository.findAll()).thenReturn(List.of(mockedCopies.get(2)));
        when(rentedCopyService.getRentedCopy(isA(Integer.class))).thenReturn(mockedRentedCopies.get(0));

        // then
        assertThrows(NoAvailableCopyException.class, () -> copyService.rentCopy(movieId));
    }

    @Test
    @WithMockUser(username = "test")
    public void testReturnMovieWhenCopyIsCurrentlyRentedByTheAuthenticatedUser() {
        // given
        Integer copyId = 1;

        // when
        when(rentedCopyService.setReturnedStatus(isA(Integer.class))).thenReturn(mockedRentedCopies.get(0));
        Copy returnedCopy = copyService.returnCopy(copyId);

        // then
        verify(rentedCopyService).setReturnedStatus(copyId);
        assertEquals(3, returnedCopy.getId());
    }

    @Test
    @WithMockUser(username = "test")
    public void testReturnMovieWhenCopyIsNotRentedByTheAuthenticatedUser() {
        // given
        Integer copyId = 1;

        // when
        when(rentedCopyService.setReturnedStatus(copyId)).thenThrow(ReturnCopyException.class);

        // then
        assertThrows(ReturnCopyException.class, () -> copyService.returnCopy(copyId));
    }
}