package com.sprintBatch.springBatch.repository;

import com.sprintBatch.springBatch.domain.Director;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    Optional<Director> findByName(String directorName);
}
