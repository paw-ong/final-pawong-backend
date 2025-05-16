package kr.co.pawong.pwbe.adoption.adapter.in.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class AdoptionJobScheduler {
    private final JobLauncher jobLauncher;
    private final Job adoptionApiJob;

    @Scheduled(cron = "0 0 * * * ?")
    public void runAdoptionApiJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(adoptionApiJob, jobParameters);
        } catch (Exception e) {
            log.error("배치 작업 실행 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
