package kr.co.pawong.pwbe.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Slf4j
@Configuration
public class FcmConfig {

    @Value("${app.firebase-configuration-file}")
    private Resource firebaseConfig;   // Resource 타입으로 변경

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        try (InputStream in = firebaseConfig.getInputStream()) {
            // 한 번만 스트림을 읽어서 credentials 생성
            GoogleCredentials credentials = GoogleCredentials.fromStream(in);

            // credentials 객체 재사용
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            FirebaseApp app = FirebaseApp.getApps().isEmpty()
                    ? FirebaseApp.initializeApp(options)
                    : FirebaseApp.getInstance();

            log.info("FirebaseApp initialization 성공: {}", app.getName());
            return app;
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp app) {
        return FirebaseMessaging.getInstance(app);
    }
}