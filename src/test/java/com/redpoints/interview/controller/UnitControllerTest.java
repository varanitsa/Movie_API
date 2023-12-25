package com.redpoints.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redpoints.interview.mappers.MovieMapper;
import com.redpoints.interview.model.Movie;
import com.redpoints.interview.service.MovieService;
import com.redpoints.interview.service.data.MovieEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UnitControllerTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;

    @Mock
    private MovieMapper movieMapper;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }


    @Test
    public void getAllMoviesTest() throws Exception {

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Titanic", "James Cameron", 1997));
        movies.add(new Movie("Aviator", "Martin Scorsese", 2004));
        movies.add(new Movie("Madagascar", "Tom McGrath, Eric Darnell", 2005));


        when(movieMapper.entitiesToModels(anyList())).thenReturn(movies);

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title").value("Titanic"))
                .andExpect(jsonPath("$[0].director").value("James Cameron"))
                .andExpect(jsonPath("$[0].year").value(1997))
                .andExpect(jsonPath("$[1].title").value("Aviator"))
                .andExpect(jsonPath("$[1].director").value("Martin Scorsese"))
                .andExpect(jsonPath("$[1].year").value(2004))
                .andExpect(jsonPath("$[2].title").value("Madagascar"))
                .andExpect(jsonPath("$[2].director").value("Tom McGrath, Eric Darnell"))
                .andExpect(jsonPath("$[2].year").value(2005));

        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    void shouldGetMovieByIdTest() throws Exception {


        Movie movie = new Movie("Titanic", "James Cameron", 1997);
        movie.setId(1L);

        MovieEntity movieEntity = new MovieEntity("Titanic", "James Cameron", 1997);
        movieEntity.setId(1L);

        when(movieService.getMovieById(1L)).thenReturn(Optional.of(movieEntity));
        when(movieMapper.entityToModel(movieEntity)).thenReturn(movie);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movies/{id}", movie.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.director").value(movie.getDirector()))
                .andExpect(jsonPath("$.year").value(movie.getYear()));


        verify(movieService, times(1)).getMovieById(1L);
    }

    @Test
    void shouldMovieNotFoundTest() throws Exception {

        when(movieService.getMovieById(1L)).thenReturn(Optional.empty());

        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movies/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());


        verify(movieService, times(1)).getMovieById(1L);
    }

}

