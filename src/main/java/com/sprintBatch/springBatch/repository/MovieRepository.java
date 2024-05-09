package com.sprintBatch.springBatch.repository;

import com.sprintBatch.springBatch.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, String> {
    Optional<Movie> findByMovieCd(String movieCd);
}
