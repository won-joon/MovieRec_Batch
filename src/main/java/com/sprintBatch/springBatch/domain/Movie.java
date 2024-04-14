package com.sprintBatch.springBatch.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Movie {

    @Id
    private String movie_cd;

    private String movie_nm;

    private String genre_alt;

    private String open_dt;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieDirector> director_list = new ArrayList<>();


    @Builder
    public Movie(String movie_cd, String movie_nm, String genre_alt, String open_dt){
        this.movie_cd = movie_cd;
        this.movie_nm = movie_nm;
        this.genre_alt = genre_alt;
        this.open_dt = open_dt;
    }

    public void addDirector(MovieDirector movieDirector) {
        director_list.add(movieDirector);
        movieDirector.setMovie(this);
    }

}
