package kr.co.pawong.pwbe.adoption.application.service;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class AdoptionJobScheduler {
    private final JobLauncher jobLauncher;
    private final Job adoptionApiJob;

    public AdoptionJobScheduler(JobLauncher jobLauncher, @Qualifier("adoptionApiJob") Job adoptionApiJob) {
        this.jobLauncher = jobLauncher;
        this.adoptionApiJob = adoptionApiJob;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void runAdoptionApiJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            log.info("유기동물 API 배치 작업 시작: {}", LocalDateTime.now());
            jobLauncher.run(adoptionApiJob, jobParameters);
        } catch (Exception e) {
            log.error("배치 작업 실행 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
