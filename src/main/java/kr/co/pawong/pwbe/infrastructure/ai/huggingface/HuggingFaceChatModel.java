package kr.co.pawong.pwbe.infrastructure.ai.huggingface;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @reference: https://huggingface.co/docs/inference-providers/tasks/chat-completion
 * @implements: ChatModel - 스프링 AI 라이브러리에서 만들어 놓은 채팅 전용 인터페이스입니다. 이를 구현해서 HuggingFace Inference API
 * 기반의 채팅 모델을 만들었습니다. 사용자 메시지를 기반으로 AI 모델의 응답을 생성하는 기능을 제공합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class HuggingFaceChatModel implements ChatModel {

    private final int MAX_TOKEN_LENGTH = 200;

    private final RetryTemplate retryTemplate;
    private final WebClient webClient;     // baseUrl="https://router.huggingface.co/hf-inference"
    private final String modelName;        // ex) "Qwen/QwQ-32B"
    private final ChatOptions defaultOptions = ChatOptions.builder().maxTokens(MAX_TOKEN_LENGTH)
            .build();

    @Override
    public ChatResponse call(Prompt prompt) {
        // 1) prompt.getInstructions() → HF 스펙 메시지 리스트로 변환
        List<Map<String, String>> hfMessages = prompt.getInstructions().stream()
                .map(m -> Map.of(
                        "role", m.getMessageType().getValue(),
                        "content", m.getContent()
                ))
                .toList();

        // 2) 요청 바디 구성 (스펙 기반)
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("messages", hfMessages);
        body.put("max_tokens", defaultOptions.getMaxTokens());
        body.put("stream", false);

        // retryTemplate을 통해서 기본 3번 재시도(네트워크 에러를 비롯한 언체크 예외라도 발생하면 재시도함.
        return retryTemplate.execute(
                (ctx) -> {
                    log.info("HuggingFace 챗 요청 시도 {}회차", ctx.getRetryCount() + 1);
                    // 3) HF Inference API 호출
                    JsonNode resp = webClient.post()
                            .uri("/models/{model}/v1/chat/completions", modelName)
                            .bodyValue(body)
                            .retrieve()
                            .bodyToMono(JsonNode.class)
                            .block();

                    // 4) 스펙에 맞춰 안전하게 파싱
                    List<Generation> gens = parseGenerations(resp);

                    // 5) ChatResponse 생성
                    return ChatResponse.builder()
                            .generations(gens)
                            .build();
                },
                // 모든 재시도에서 실패하면 아래 로직을 수행
                (ctx) -> {
                    log.error("HuggingFace 챗 요청 {}회 모두 실패했습니다.",
                            ctx.getRetryCount());
                    throw new RuntimeException("HuggingFace 챗 요청할 때 예외 발생", ctx.getLastThrowable());
                }
        );
    }

    private static List<Generation> parseGenerations(JsonNode resp) {
        List<Generation> gens = new ArrayList<>();
        JsonNode choices = resp.path("choices");
        if (choices.isArray()) {
            for (JsonNode choice : choices) {
                String content = choice
                        .path("message")
                        .path("content")
                        .asText("");
                AssistantMessage msg = new AssistantMessage(content);
                gens.add(new Generation(msg));
            }
        }
        return gens;
    }

    @Override
    public ChatOptions getDefaultOptions() {
        return defaultOptions;
    }
}