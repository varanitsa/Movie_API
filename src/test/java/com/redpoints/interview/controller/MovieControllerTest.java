package com.redpoints.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redpoints.interview.mappers.MovieMapper;
import com.redpoints.interview.model.Movie;
import com.redpoints.interview.service.MovieService;
import com.redpoints.interview.service.data.MovieEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MovieController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class MovieControllerTest {

    @MockBean
    private MovieService movieService;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private MovieMapper movieMapper;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void getAllMoviesTest() throws Exception {

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Titanic", "James Cameron", 1997));
        movies.add(new Movie("Aviator", "Martin Scorsese", 2004));
        movies.add(new Movie("Madagascar", "Tom McGrath, Eric Darnell", 2005));


        List<MovieEntity> movieEntities = new ArrayList<>();
        movieEntities.add(new MovieEntity("Titanic", "James Cameron", 1997));
        movieEntities.add(new MovieEntity("Aviator", "Martin Scorsese", 2004));
        movieEntities.add(new MovieEntity("Madagascar", "Tom McGrath, Eric Darnell", 2005));


        when(movieService.getAllMovies()).thenReturn(movieEntities);
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
    }


    @Test
    void shouldGetNoMoviesTest() throws Exception {
        List<Movie> movies = new ArrayList<>();
        List<MovieEntity> movieEntities = new ArrayList<>();

        when(movieService.getAllMovies()).thenReturn(movieEntities);
        when(movieMapper.entitiesToModels(anyList())).thenReturn(movies);

        mockMvc.perform(get("/movies"))
                .andExpect(status().isNoContent());

    }

    @Test
    void getMovieByIdTest() throws Exception {
        Movie movie = new Movie("Titanic", "James Cameron", 1997);
        movie.setId(1L);

        MovieEntity movieEntity = new MovieEntity("Titanic", "James Cameron", 1997);
        movieEntity.setId(1L);

        when(movieService.getMovieById(1L)).thenReturn(Optional.of(movieEntity));
        when(movieMapper.entityToModel(movieEntity)).thenReturn(movie);


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movies/{id}", movie.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.director").value(movie.getDirector()))
                .andExpect(jsonPath("$.year").value(movie.getYear()));

        verify(movieService, times(1)).getMovieById(1L);

    }


    @Test
    void shouldNotGetAnyMoviesByIdTest() throws Exception {
        Long id = 2L;

        when(movieService.getMovieById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/movies" + id))
                .andExpect(status().isNotFound());

    }
}






