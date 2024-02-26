package com.redpoints.interview.repository;

import com.redpoints.interview.service.data.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {


    Optional<MovieEntity> findByTitleIgnoreCase(String title);
}
