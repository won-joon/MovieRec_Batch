package com.sprintBatch.springBatch.repository;

import com.sprintBatch.springBatch.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, String> {
}
