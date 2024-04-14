package com.sprintBatch.springBatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Director {

    @Id
    @GeneratedValue
    @Column(name = "director_id")
    private Long id;

    private String name;

    @Builder
    public Director(String name){
        this.name = name;
    }
}
