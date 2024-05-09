package com.sprintBatch.springBatch.dto;

import com.sprintBatch.springBatch.domain.Movie;
import com.sprintBatch.springBatch.domain.MovieDirector;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MovieDto {
    private String movieCd;

    private String movieNm;

    private String genreAlt;

    private String openDt;

    private List<String> directorList;


    public Movie toEntity(){
        // Movie 객체를 생성하고 반환하는 로직
        return Movie.builder()
                .movieCd(movieCd)
                .movieNm(movieNm)
                .genreAlt(genreAlt)
                .openDt(openDt)
                .build();
    }
}
