package com.redpoints.interview.service;

import com.redpoints.interview.service.data.MovieEntity;
import com.redpoints.interview.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

	private final MovieRepository repository;


	public MovieService(MovieRepository repository) {
		this.repository = repository;
	}

	public List<MovieEntity> getAllMovies() {
		return repository.findAll();
	}

	public Optional<MovieEntity> getMovieById(Long id) {
		return repository.findById(id);
	}

	public MovieEntity createMovie(MovieEntity movieEntity) {
		return repository.save(movieEntity);
	}


	public Optional<MovieEntity> updateMovieById(Long id, MovieEntity updatedMovieEntity) {
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
}


