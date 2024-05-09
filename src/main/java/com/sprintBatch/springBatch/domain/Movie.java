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
    private String movieCd;

    private String movieNm;

    private String genreAlt;

    private String openDt;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieDirector> director_list = new ArrayList<>();


    @Builder
    public Movie(String movieCd, String movieNm, String genreAlt, String openDt){
        this.movieCd = movieCd;
        this.movieNm = movieNm;
        this.genreAlt = genreAlt;
        this.openDt = openDt;
    }

    public void addDirector(MovieDirector movieDirector) {
        director_list.add(movieDirector);
        movieDirector.setMovie(this);
    }

}
