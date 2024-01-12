package com.redpoints.interview.controller;

import com.redpoints.interview.mappers.MovieMapper;
import com.redpoints.interview.model.Movie;
import com.redpoints.interview.service.MovieService;
import com.redpoints.interview.service.data.MovieEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @Autowired
    public MovieController(MovieService movieService, MovieMapper movieMapper) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }


    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<MovieEntity> movieEntities = movieService.getAllMovies();
        List<Movie> movies = movieMapper.entitiesToModels(movieEntities);

        if (movies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") Long id) {
        Optional<MovieEntity> movieEntity = movieService.getMovieById(id);

        if (movieEntity.isPresent()) {
            Movie movie = movieMapper.entityToModel(movieEntity.get());
            return new ResponseEntity<>(movie, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Movie> getMovieByTitle(@PathVariable("title") String title) {
        Optional<MovieEntity> movieEntity = movieService.getMovieByTitle(title);

        if (movieEntity.isPresent()) {
            Movie movie = movieMapper.entityToModel(movieEntity.get());
            return new ResponseEntity<>(movie, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) Integer year
    ) {
        List<MovieEntity> movieEntities = movieService.searchMovies(title, director, year);

        if (!movieEntities.isEmpty()) {
            List<Movie> movies = movieEntities.stream()
                    .map(movieMapper::entityToModel)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return Optional.of(movie)
                .map(movieMapper::modelToEntity)
                .map(movieService::createMovie)
                .map(movieMapper::entityToModel)
                .map(createdMovie -> new ResponseEntity<>(createdMovie, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovieById(@PathVariable Long id, @RequestBody Movie updatedMovie) {
        Optional<MovieEntity> existingMovieEntity = movieService.getMovieById(id);

        return existingMovieEntity.map(movieEntity -> {
            Optional<MovieEntity> updatedMovieEntityOptional = movieService.updateMovieById(id, movieMapper.modelToEntity(updatedMovie));

            return updatedMovieEntityOptional.map(updatedMovieEntity -> {
                Movie updatedMovieModel = movieMapper.entityToModel(updatedMovieEntity);
                return new ResponseEntity<>(updatedMovieModel, HttpStatus.OK);
            }).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMovieById(@PathVariable("id") long id) {
        if (movieService.deleteMovieById(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllMovies() {
        movieService.deleteAllMovies();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}












