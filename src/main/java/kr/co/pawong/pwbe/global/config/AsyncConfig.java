package kr.co.pawong.pwbe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
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
}
