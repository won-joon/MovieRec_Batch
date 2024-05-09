package com.sprintBatch.springBatch.batch;

import com.sprintBatch.springBatch.domain.Director;
import com.sprintBatch.springBatch.domain.Movie;
import com.sprintBatch.springBatch.domain.MovieDirector;
import com.sprintBatch.springBatch.dto.MovieDto;
import com.sprintBatch.springBatch.repository.DirectorRepository;
import com.sprintBatch.springBatch.repository.MovieRepository;
import com.sprintBatch.springBatch.utils.GetDataUtils;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@Slf4j
public class MovieBatchConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private GetDataUtils getDataUtils;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private MovieRepository movieRepository;


    @Bean
    public Job myJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("myJob", jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step", jobRepository)
                .<MovieDto, Movie>chunk(10, platformTransactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ItemReader<MovieDto> itemReader() {
        List<MovieDto> movieDtoList = getDataUtils.getMovieListData();
        List<MovieDto> movieList = new ArrayList<>();
        for(MovieDto movieDto : movieDtoList){  // 영화 데이터 중복 처리
            Optional<Movie> movie = movieRepository.findByMovieCd(movieDto.getMovieCd());
            if(movie.isEmpty()){
                movieList.add(movieDto);
            }
        }

        return new ListItemReader<>(movieList);
    }

    @Bean
    public ItemProcessor<MovieDto, Movie> itemProcessor() {
        log.info("배포 진행중");
        return item -> {
            Movie movie = item.toEntity();
            if (item.getDirectorList() != null) {
                for (String directorName : item.getDirectorList()) {
                    Optional<Director> existingDirector = directorRepository.findByName(directorName);
                    MovieDirector movieDirector = new MovieDirector();

                    if(existingDirector.isPresent()){  // 중복 감독 처리
                        movieDirector.setDirector(existingDirector.get());
                    }else{
                        Director director = directorRepository.save(Director.builder().name(directorName).build());
                        movieDirector.setDirector(director);
                    }

                    movie.addDirector(movieDirector);
                }
            }

            return movie;
        };
    }

    @Bean
    public JpaItemWriter<Movie> itemWriter() {
        JpaItemWriter<Movie> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
