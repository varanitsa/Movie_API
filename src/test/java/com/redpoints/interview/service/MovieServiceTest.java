package com.redpoints.interview.service;

import com.redpoints.interview.repository.MovieRepository;
import com.redpoints.interview.service.data.MovieEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class MovieServiceTest {
    @Mock
    private MovieRepository repository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllMoviesTest() {

        List<MovieEntity> movies = new ArrayList<>();
        movies.add(new MovieEntity("Titanic", "James Cameron", 1997));
        movies.add(new MovieEntity("Aviator", "Martin Scorsese", 2004));
        movies.add(new MovieEntity("Madagascar", "Tom McGrath, Eric Darnell", 2005));

        when(repository.findAll()).thenReturn(movies);

        List<MovieEntity> result = movieService.getAllMovies();
        assertEquals(3, result.size());
    }

    @Test
    void searchMovieLowerCaseTest() {

        MovieEntity movie1 = new MovieEntity("Titanic", "James Cameron", 1997);
        MovieEntity movie2 = new MovieEntity("Aviator", "Martin Scorsese", 2004);
        MovieEntity movie3 = new MovieEntity("Madagascar", "Tom MgGrath, Eric Darnell", 2005);
        MovieEntity movie4 = new MovieEntity("Avatar", "James Cameron", 2009);
        List<MovieEntity> movies = Arrays.asList(movie1, movie2, movie3, movie4);

        when(repository.findAll()).thenReturn(movies);

        List<MovieEntity> result = movieService.searchMovies(null, "james",null);

        assertThat(result).containsExactly(movie1,movie4);

    }

    @Test
    void getMovieByIdTest() {

        Long movieId = 1L;
        MovieEntity movieEntity = new MovieEntity("Titanic", "James Cameron", 1997);

        when(repository.findById(movieId)).thenReturn(Optional.of(movieEntity));

        Optional<MovieEntity> result = movieService.getMovieById(movieId);

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(movieEntity);
    }

    @Test
    void getMovieById_notFoundTest() {

        Long movieId = 1L;

        when(repository.findById(movieId)).thenReturn(Optional.empty());

        Optional<MovieEntity> result = movieService.getMovieById(movieId);

        assertThat(result).isEmpty();
    }

    @Test
    void getMovieByTitleIgnoreCaseTest() {

        String movieTitle = "titanic";
        MovieEntity movieEntity = new MovieEntity("Titanic", "James Cameron", 1997);

        when(repository.findByTitleIgnoreCase("Titanic")).thenReturn(Optional.of(movieEntity));

        Optional<MovieEntity> result = movieService.getMovieByTitle("Titanic");

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(movieEntity);
    }

    @Test
    void getMovieByTitle_notFoundTest() {


        String movieTitle = "Titanic";

        when(repository.findByTitleIgnoreCase(movieTitle)).thenReturn(Optional.empty());

        Optional<MovieEntity> result = movieService.getMovieByTitle(movieTitle);

        assertThat(result).isEmpty();
    }


    @Test
    void createMovieTest() {

        MovieEntity movieToCreate = new MovieEntity("Titanic", "James Cameron", 1997);
        MovieEntity createdMovie = new MovieEntity("Titanic", "James Cameron", 1997);

        when(repository.save(movieToCreate)).thenReturn(createdMovie);

        MovieEntity result = movieService.createMovie(movieToCreate);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Titanic");
        assertThat(result.getDirector()).isEqualTo("James Cameron");
        assertThat(result.getYear()).isEqualTo(1997);
    }


    @Test
    void createMovieWithNegativeYearTest() {
        MovieEntity movieWithNegativeYear = new MovieEntity("Titanic", "James Cameron", -1997);

        assertThrows(IllegalArgumentException.class, () -> {
            movieService.createMovie(movieWithNegativeYear);
        });

        verify(repository, never()).save(any());
    }

    @Test
    void createMovieWithNullDataTest() {
        MovieEntity movieWithNullData = new MovieEntity(null, "James Cameron", 1997);

        assertThrows(IllegalArgumentException.class, () -> {
            movieService.createMovie(movieWithNullData);
        });

        verify(repository, never()).save(any());
    }


    @Test
    void updateMovieByIdTest() {

        Long movieId = 1L;
        MovieEntity existingMovieEntity = new MovieEntity("Titanik", "James Cameron", 2020);
        MovieEntity updatedMovieEntity = new MovieEntity("Titanic", "James Cameron", 1997);

        when(repository.findById(movieId)).thenReturn(Optional.of(existingMovieEntity));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<MovieEntity> result = movieService.updateMovieById(movieId, updatedMovieEntity);

        assertTrue(result.isPresent(), "The movie should be present after the update.");
        assertEquals(updatedMovieEntity.getTitle(), result.get().getTitle(), "The title should be updated");
        assertEquals(updatedMovieEntity.getDirector(), result.get().getDirector(), "The director should be updated");
        assertEquals(updatedMovieEntity.getYear(), result.get().getYear(), "The year should be updated");

        verify(repository, times(1)).findById(movieId);
        verify(repository, times(1)).save(existingMovieEntity);
    }

    @Test
    void updateMovieWithNullDataTest() {
        Long movieId = 1L;
        MovieEntity movieWithNullData = new MovieEntity(null, "Updated Director", 2000);

        assertThrows(IllegalArgumentException.class, () -> {
            movieService.updateMovieById(movieId, movieWithNullData);
        });

        verify(repository, never()).save(any());
    }

    @Test
    void updateMovieWithNegativeYearTest() {
        Long movieId = 1L;
        MovieEntity movieWithNegativeYear = new MovieEntity("Updated Title", "Updated Director", -2000);

        assertThrows(IllegalArgumentException.class, () -> {
            movieService.updateMovieById(movieId, movieWithNegativeYear);
        });

        verify(repository, never()).save(any());
    }


    @Test
    void deleteMovieByIdTest() {
        Long movieId = 1L;
        MovieEntity existingMovieEntity = new MovieEntity("Titanic", "James Cameron", 1997);

        when(repository.findById(movieId)).thenReturn(Optional.of(existingMovieEntity));

        boolean result = movieService.deleteMovieById(movieId);

        assertTrue(result, "The movie should exist and be deleted");

        verify(repository, times(1)).deleteById(movieId);
    }


    @Test
    void deleteAllMoviesTest() {

        movieService.deleteAllMovies();
        verify(repository, times(1)).deleteAll();
    }
}



