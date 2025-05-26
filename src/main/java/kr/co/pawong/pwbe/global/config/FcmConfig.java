package kr.co.pawong.pwbe.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
public class FcmConfig {

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    // 스프링 빈 초기화 후 자동으로 실행
    // Firebase Admin SDK를 초기화하여 FCM 서비스를 사용할 준비
    @PostConstruct
    public void initialize() {
        try {
            // 리소스 폴더에서 Firebase 서비스 계정 키 파일 로드
            FileInputStream serviceAccount = new FileInputStream(new ClassPathResource(firebaseConfigPath).getFile());

            // Firebase 옵션 설정 - 서비스 계정 인증 정보 포함
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // Firebase 앱이 이미 초기화되지 않앗다면 초기화
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("파이어베이스가 실행되었습니다.");
            }
        } catch (IOException e) {
            // 초기화 중 오류 발생 시 로그
            log.error("파이어베이스 실행 오류", e);
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        return FirebaseMessaging.getInstance();
    }
}
