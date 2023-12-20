package com.redpoints.interview.service;


import com.redpoints.interview.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class MovieServiceTest {
    @Mock
    private MovieRepository repository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}
