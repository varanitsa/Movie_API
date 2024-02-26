package com.redpoints.interview.service;

import com.redpoints.interview.mappers.MovieMapper;
import com.redpoints.interview.model.Movie;
import com.redpoints.interview.service.data.MovieEntity;
import com.redpoints.interview.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository repository;

    private final MovieMapper movieMapper;


    public MovieService(MovieRepository repository, MovieMapper movieMapper) {
        this.repository = repository;
        this.movieMapper = movieMapper;
    }

    public List<MovieEntity> getAllMovies() {
        return repository.findAll();
    }

    public Optional<MovieEntity> getMovieById(Long id) {
        return repository.findById(id);
    }




    public MovieEntity createMovie(MovieEntity movieEntity) {

        if (movieEntity.getTitle() == null || movieEntity.getDirector() == null || movieEntity.getYear() == null) {
            throw new IllegalArgumentException("The fields title, director, and year cannot be null");
        }

        if (movieEntity.getYear() <= 0) {
            throw new IllegalArgumentException("The field year cannot be 0 or negative");
        }

        return repository.save(movieEntity);
    }

    public Optional<MovieEntity> updateMovieById(Long id, MovieEntity updatedMovieEntity) {

        if (updatedMovieEntity.getTitle() == null || updatedMovieEntity.getDirector() == null || updatedMovieEntity.getYear() == null) {
            throw new IllegalArgumentException("The fields title, director, and year cannot be null");
        }

        if (updatedMovieEntity.getYear() <= 0) {
            throw new IllegalArgumentException("The field year cannot be 0 or negative");
        }

        return repository.findById(id).map(existingMovieEntity -> {
            existingMovieEntity.setTitle(updatedMovieEntity.getTitle());
            existingMovieEntity.setDirector(updatedMovieEntity.getDirector());
            existingMovieEntity.setYear(updatedMovieEntity.getYear());
            return repository.save(existingMovieEntity);
        });
    }


    public boolean deleteMovieById(Long id) {
        return repository.findById(id)
                .map(movie -> {
                    repository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    public void deleteAllMovies() {
        repository.deleteAll();
    }


    public Optional<MovieEntity> getMovieByTitle(String title) {
        if (title == null) {
            return Optional.empty();
        }
        return repository.findByTitleIgnoreCase(title);
    }


    public List<MovieEntity> searchMovies(String title, String director, Integer year) {
        return repository.findAll().stream()
                .filter(movie ->
                        (title == null || movie.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                                (director == null || movie.getDirector().toLowerCase().contains(director.toLowerCase())) &&
                                (year == null || movie.getYear().equals(year)))
                .collect(Collectors.toList());
    }

}


