package com.redpoints.interview.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redpoints.interview.initializer.DatabaseLoader;
import com.redpoints.interview.model.Movie;
import com.redpoints.interview.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private DatabaseLoader databaseLoader;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
        databaseLoader.run();
    }

    @Test
    public void getAllMoviesTest() throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title").value("Tenet"))
                .andExpect(jsonPath("$[0].director").value("Christopher Nolan"))
                .andExpect(jsonPath("$[0].year").value(2020))
                .andExpect(jsonPath("$[1].title").value("Men"))
                .andExpect(jsonPath("$[1].director").value("Alex Garland"))
                .andExpect(jsonPath("$[1].year").value(2022))
                .andExpect(jsonPath("$[2].title").value("The Princess Bride"))
                .andExpect(jsonPath("$[2].director").value("Rob Reiner"))
                .andExpect(jsonPath("$[2].year").value(1987));
    }

    @Test
    public void createdAndReturnedByGetAllMoviesTest() throws Exception {
        movieRepository.deleteAll();

        Movie newMovie = new Movie("Titanic", "James Cameron", 1997);

        String newMovieJson = objectMapper.writeValueAsString(newMovie);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMovieJson))
                .andExpect(status().isCreated());

        MvcResult getResult = mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andReturn();

        String content = getResult.getResponse().getContentAsString();
        List<Movie> movies = objectMapper.readValue(content, new TypeReference<List<Movie>>() {
        });

        assertTrue(movies.stream().anyMatch(movie -> movie.getTitle().equals("Titanic")));
    }

}



