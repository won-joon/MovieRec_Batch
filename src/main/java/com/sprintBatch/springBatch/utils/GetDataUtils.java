package com.sprintBatch.springBatch.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprintBatch.springBatch.dto.MovieDto;
import com.sprintBatch.springBatch.dto.MovieResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetDataUtils {
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    //@Value("${search.client.key}")
    private String clientKey = "65ebffd667c42aa2290c6e39ecebd1aa";

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie")
            .build();

    public List<MovieDto> getMovieListData() {
        List<MovieDto> totalMovieList = new ArrayList<>();

        for(int page=0; page<=1; page++){
            MovieResponseDto responseDto = get(page);
            if (responseDto.movieListResult() == null) {
                System.out.println(page);
                continue;
            }

            List<MovieDto> movieList = responseDto.movieListResult().movieList().stream()
                    .map(movie -> {
                        List<String> directorList = movie.directors().stream().map(MovieResponseDto.MovieListResult.Movie.Directors::peopleNm).toList();
                        MovieDto movieDto = mapper.convertValue(movie, MovieDto.class);
                        movieDto.setDirectorList(directorList);
                        return movieDto;
                    })
                    .toList();

            totalMovieList.addAll(movieList);
        }

        return totalMovieList;
    }

    public MovieResponseDto get(Integer page) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/searchMovieList.json")
                        .queryParam("key", clientKey)
                        .queryParam("curPage", page)
                        .build())
                .retrieve()
                .bodyToMono(MovieResponseDto.class)
//                .log()
                .block(); // 동기적으로 결과를 기다리고 반환
    }
}