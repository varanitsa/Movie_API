package com.redpoints.interview.repository;

import com.redpoints.interview.service.data.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

}
