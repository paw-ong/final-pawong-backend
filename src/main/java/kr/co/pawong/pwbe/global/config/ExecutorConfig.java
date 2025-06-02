package kr.co.pawong.pwbe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ExecutorConfig {

    /**
     * ThreadPool 을 사용하는 Executor 빈 등록
     */
    @Bean("aiExecutor")
    public Executor aiExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(10);
        exec.setMaxPoolSize(20);
        exec.setQueueCapacity(100);
        exec.setThreadNamePrefix("ai-");
        exec.initialize();
        return exec;
    }

    @Bean(name = "chatExecutor")
    public ThreadPoolTaskExecutor chatExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);       // 최소 4개 스레드
        executor.setMaxPoolSize(8);        // 최대 8개 스레드
        executor.setQueueCapacity(1000);   // 대기 큐 크기
        executor.setThreadNamePrefix("chat-exec-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "notificationExecutor")
    public ThreadPoolTaskExecutor notificationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);       // 최소 10개 스레드
        executor.setMaxPoolSize(10);        // 최대 10개 스레드
        executor.setQueueCapacity(1000);   // 대기 큐 크기
        executor.setThreadNamePrefix("notify-exec-");
        executor.initialize();
        return executor;
    }

}
