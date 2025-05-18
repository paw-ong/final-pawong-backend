package kr.co.pawong.pwbe.global.config;

import kr.co.pawong.pwbe.adoption.adapter.in.batch.processor.AdoptionAiProcessor;
import kr.co.pawong.pwbe.adoption.adapter.in.batch.processor.AdoptionApiProcessor;
import kr.co.pawong.pwbe.adoption.adapter.in.batch.processor.AdoptionEsProcessor;
import kr.co.pawong.pwbe.adoption.adapter.in.batch.reader.AdoptionAiReader;
import kr.co.pawong.pwbe.adoption.adapter.in.batch.reader.AdoptionApiReader;
import kr.co.pawong.pwbe.adoption.adapter.in.batch.reader.AdoptionEsReader;
import kr.co.pawong.pwbe.adoption.adapter.in.batch.writer.AdoptionAiWriter;
import kr.co.pawong.pwbe.adoption.adapter.in.batch.writer.AdoptionApiWriter;
import kr.co.pawong.pwbe.adoption.adapter.in.batch.writer.AdoptionEsWriter;
import kr.co.pawong.pwbe.infrastructure.api.dto.AdoptionCreate;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionEsDto;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class AdoptionBatchConfig {

    @Bean
    public Job adoptionApiJob(JobRepository jobRepository,
            Step adoptionApiStep,
            Step aiProcessedStep,
            Step saveEsStep) {
        return new JobBuilder("adoptionApiJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(adoptionApiStep)
                .next(aiProcessedStep)
                .next(saveEsStep)
                .build();
    }

    @Bean
    public Step adoptionApiStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            AdoptionApiReader reader,
            AdoptionApiProcessor processor,
            AdoptionApiWriter writer) {
        return new StepBuilder("adoptionApiStep", jobRepository)
                .<AdoptionApi.Item, AdoptionCreate>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step aiProcessedStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            AdoptionAiReader reader,
            AdoptionAiProcessor processor,
            AdoptionAiWriter writer) {
        return new StepBuilder("aiProcessedStep", jobRepository)
                .<Adoption, Adoption>chunk(50, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step saveEsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            AdoptionEsReader reader,
            AdoptionEsProcessor processor,
            AdoptionEsWriter writer) {
        return new StepBuilder("saveEsStep", jobRepository)
                .<Adoption, AdoptionEsDto>chunk(50, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}

