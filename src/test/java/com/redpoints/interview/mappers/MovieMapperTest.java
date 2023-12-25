package com.redpoints.interview.mappers;

import com.redpoints.interview.model.Movie;
import com.redpoints.interview.service.data.MovieEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MovieMapper.class})
public class MovieMapperTest {

    @Autowired
    private MovieMapper movieMapper;

    @Test
    public void testEntityToModel() {

        MovieEntity movieEntity = new MovieEntity("Titanic", "James Cameron", 1997);


        Movie movie = movieMapper.entityToModel(movieEntity);


        assertThat(movie.getTitle()).isEqualTo("Titanic");
        assertThat(movie.getDirector()).isEqualTo("James Cameron");
        assertThat(movie.getYear()).isEqualTo(1997);
    }

    @Test
    public void testModelToEntity() {

        Movie movie = new Movie("Titanic", "James Cameron", 1997);


        MovieEntity movieEntity = movieMapper.modelToEntity(movie);


        assertThat(movieEntity.getTitle()).isEqualTo("Titanic");
        assertThat(movieEntity.getDirector()).isEqualTo("James Cameron");
        assertThat(movieEntity.getYear()).isEqualTo(1997);
    }
}

