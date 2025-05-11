package kr.co.pawong.pwbe.infrastructure.ai.huggingface;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class HuggingFaceConfig {

    public static final String HUGGINGFACE_WEB_CLIENT_BEAN_NAME = "huggingfaceWebClient";

    @Value("${spring.ai.huggingface.chat.url}")
    private String baseUrl;
    @Value("${spring.ai.huggingface.chat.api-key}")
    private String apiKey;
    @Value("${spring.ai.huggingface.embedding.model}")
    private String embeddingModelName;
    @Value("${spring.ai.huggingface.chat.model}")
    private String chatModelName;

    // 다른 AI API 이용 시에 또 다른 WebClient가 필요할 수 있어서 Bean 이름을 따로 지정해서 사용했습니다.
    @Bean(HUGGINGFACE_WEB_CLIENT_BEAN_NAME)
    public WebClient huggingFaceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    // 임베딩을 위한 Embedding Model 빈 등록
    @Bean
    public HuggingFaceEmbeddingModel huggingFaceEmbeddingModel(
            @Qualifier(HUGGINGFACE_WEB_CLIENT_BEAN_NAME) WebClient webClient
    ) {
        return new HuggingFaceEmbeddingModel(retryTemplate(), webClient, embeddingModelName);
    }

    // Chat을 위한 Chat Model 빈 등록
    @Bean
    public HuggingFaceChatModel huggingFaceChatModel(
            @Qualifier(HUGGINGFACE_WEB_CLIENT_BEAN_NAME) WebClient webClient
    ) {
        return new HuggingFaceChatModel(retryTemplate(), webClient, chatModelName);
    }

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(3)              // 기본 3회 시도
                .fixedBackoff(1_000)         // 재시도 간격 1초
                .retryOn(Exception.class)    // 재시도할 예외 지정
                .build();
    }
}
