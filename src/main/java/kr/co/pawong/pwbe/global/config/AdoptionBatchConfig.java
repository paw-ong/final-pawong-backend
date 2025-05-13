package kr.co.pawong.pwbe.global.config;

import kr.co.pawong.pwbe.adoption.application.port.in.ApiRequestUseCase;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration
public class AdoptionBatchConfig {

    @Bean
    public Job adoptionApiJob(JobRepository jobRepository, Step adoptionApiStep) {
        return new JobBuilder("adoptionApiJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(adoptionApiStep)
                .build();
    }

    @Bean
    public Step adoptionApiStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            ApiRequestUseCase apiRequestUseCase) {
        return new StepBuilder("adoptionApiStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    apiRequestUseCase.fetchAndSaveAdoptions();
                    return RepeatStatus.FINISHED; // 한 번만 실행
                }, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }
}

