package kr.co.pawong.pwbe.adoption.application.service;

import kr.co.pawong.pwbe.infrastructure.ai.port.ChatProcessorPort;
import kr.co.pawong.pwbe.infrastructure.ai.port.EmbeddingProcessorPort;
import kr.co.pawong.pwbe.infrastructure.ai.AdoptionAiService;
import kr.co.pawong.pwbe.infrastructure.ai.AdoptionAiExecutor;
import kr.co.pawong.pwbe.adoption.fake.FakeEmbeddingAdapter;
import kr.co.pawong.pwbe.adoption.fake.FakeChatAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.*;

class AdoptionAiServiceTest {

    private AdoptionAiService service;

    @BeforeEach
    void setUp() {
        // 동기 실행용 executor
        Executor syncExec = Runnable::run;
        AdoptionAiExecutor executor = new AdoptionAiExecutor(syncExec);

        // fake adapter 준비
        ChatProcessorPort fakeChat = new FakeChatAdapter();
        EmbeddingProcessorPort fakeEmb = new FakeEmbeddingAdapter();

        service = new AdoptionAiService(fakeEmb, fakeChat, executor);
    }

    /**
     * embed
     */
    @Test
    void 임베딩_정상_요청() {
        // Given
        String input = "test";
        // When
        float[] output = service.embed(input);
        // Then
        assertThat(output).isEqualTo(new float[]{1.01f, 1.02f});
    }

    @Test
    void 임베딩에_null이_입력된_경우_제로_벡터가_반환됨() {
        // Given
        String input = null;
        // When
        float[] output = service.embed(input);
        // Then
        assertThat(output).isEqualTo(new float[AdoptionAiService.EMBEDDING_DIMENSION]);
    }

    @Test
    void 임베딩에_빈_문자열이_입력된_경우_제로_벡터가_반환됨() {
        // Given
        String input = "";
        // When
        float[] output = service.embed(input);
        // Then
        assertThat(output).isEqualTo(new float[AdoptionAiService.EMBEDDING_DIMENSION]);
    }

    @Test
    void 임베딩에_공백이_입력된_경우_제로_벡터가_반환됨() {
        // Given
        String input = "    \n   ";
        // When
        float[] output = service.embed(input);
        // Then
        assertThat(output).isEqualTo(new float[AdoptionAiService.EMBEDDING_DIMENSION]);
    }

    /**
     * refine
     */
    @Test
    void 문자열_정제_정상_요청() {
        // Given
        String input = "test";
        // When
        String output = service.refineSpecialMark(input);
        // Then
        assertThat(output).isEqualTo(input);
    }

    @Test
    void 문자열_정제에_null이_입력된_경우_빈_문자열이_반환됨() {
        // Given
        String input = null;
        // When
        String output = service.refineSpecialMark(input);
        // When & Then
        assertThat(output).isEqualTo("");
    }

    @Test
    void 문자열_정제에_빈_문자열이_입력된_경우_빈_문자열이_반환됨() {
        // Given
        String input = "";
        // When
        String output = service.refineSpecialMark(input);
        // When & Then
        assertThat(output).isEqualTo("");
    }

    @Test
    void 문자열_정제에_공백이_입력된_경우_빈_문자열이_반환됨() {
        // Given
        String input = "  \n  ";
        // When
        String output = service.refineSpecialMark(input);
        // When & Then
        assertThat(output).isEqualTo("");
    }

    /**
     * tag
     */
    @Test
    void 태깅_정상_요청() {
        // Given
        String input = "test";
        // When
        List<String> output = service.tag(input);
        // Then
        assertThat(output).isEqualTo(List.of("정이많음", "사람을좋아함"));
    }

    @Test
    void 태깅_시에_null이_입력된_경우_빈_배열이_반환됨() {
        // Given
        String input = null;
        // When
        List<String> output = service.tag(input);
        // Then
        assertThat(output)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void 태깅_시에_빈_문자열이_입력된_경우_빈_배열이_반환됨() {
        // Given
        String input = "";
        // When
        List<String> output = service.tag(input);
        // Then
        assertThat(output)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void 태깅_시에_공백이_입력된_경우_빈_배열이_반환됨() {
        // Given
        String input = "  \n  ";
        // When
        List<String> output = service.tag(input);
        // Then
        assertThat(output)
                .isNotNull()
                .isEmpty();
    }

    /**
     * embed batch
     */
    @Test
    void 임베딩_병렬_작업이_모두_성공한_경우() {
        // Given
        List<String> texts = List.of("test1", "test2");
        // When
        List<Optional<float[]>> results = service.embedBatch(texts);
        // Then
        assertThat(results)
                .hasSize(2)
                .satisfiesExactly(
                        opt0 -> assertThat(opt0).get().isEqualTo(new float[]{1.01f, 1.02f}),
                        opt1 -> assertThat(opt1).get().isEqualTo(new float[]{1.01f, 1.02f})
                );
    }

    @Test
    void 임베딩_병렬_작업에_실패가_포함된_경우_해당값은_제로_벡터가_됨() {
        // Given
        // "fail" 입력시 예외 발생
        List<String> texts = Arrays.asList("ok", "fail", "zzz", null, "    ");
        // When
        List<Optional<float[]>> results = service.embedBatch(texts);
        // Then
        assertThat(results)
                .hasSize(5)
                .satisfiesExactly(
                        opt0 -> assertThat(opt0).get().isEqualTo(new float[]{1.01f, 1.02f}),
                        opt1 -> assertThat(opt1).isEmpty(),
                        opt2 -> assertThat(opt2).get().isEqualTo(new float[]{1.01f, 1.02f}),
                        opt3 -> assertThat(opt3).get()
                                .isEqualTo(new float[AdoptionAiService.EMBEDDING_DIMENSION]),
                        opt4 -> assertThat(opt4).get()
                                .isEqualTo(new float[AdoptionAiService.EMBEDDING_DIMENSION])
                );
    }

    /**
     * refine batch
     */
    @Test
    void 문자열_정제_병렬_작업이_모두_성공한_경우() {
        // Given
        List<String> inputs = List.of("one", "two");
        // When
        List<Optional<String>> results = service.refineSpecialMarkBatch(inputs);
        // Then
        assertThat(results)
                .hasSize(2)
                .containsExactly(
                        Optional.of("test"),
                        Optional.of("test")
                );
    }

    @Test
    void 문자열_정제_병렬_작업에_실패가_포함된_경우_해당값은_Optional_Empty_가_됨() {
        // Given
        // "fail" 입력시 예외 발생
        List<String> inputs = Arrays.asList("ok", "fail", "also", null, "\n \t");
        // When
        List<Optional<String>> results = service.refineSpecialMarkBatch(inputs);
        // Then
        assertThat(results)
                .hasSize(5)
                .satisfiesExactly(
                        opt0 -> assertThat(opt0).contains("test"),
                        opt1 -> assertThat(opt1).isEmpty(),
                        opt2 -> assertThat(opt2).contains("test"),
                        opt3 -> assertThat(opt3).get().isEqualTo(""),
                        opt4 -> assertThat(opt4).get().isEqualTo("")
                );
    }

    /**
     * tag batch
     */
    @Test
    void 태깅_병렬_작업이_모두_성공한_경우() {
        // Given
        List<String> features = List.of("a", "b");
        // When
        List<Optional<List<String>>> results = service.tagBatch(features);
        // Then
        assertThat(results)
                .hasSize(2)
                .containsExactly(
                        Optional.of(List.of("정이많음", "사람을좋아함")),
                        Optional.of(List.of("정이많음", "사람을좋아함"))
                );
    }

    @Test
    void 태깅_병렬_작업에_실패가_포함된_경우_Optional_Empty_가_됨() {
        // Given
        List<String> features = Arrays.asList("x", "fail", "y", null, " \t \n  ");
        // When
        List<Optional<List<String>>> results = service.tagBatch(features);
        // Then
        assertThat(results)
                .hasSize(5)
                .satisfiesExactly(
                        opt0 -> assertThat(opt0).contains(List.of("정이많음", "사람을좋아함")),
                        opt1 -> assertThat(opt1).isEmpty(),
                        opt2 -> assertThat(opt2).contains(List.of("정이많음", "사람을좋아함")),
                        opt3 -> {
                            assertThat(opt3).isPresent();
                            assertThat(opt3.get()).isEmpty();
                        },
                        opt4 -> {
                            assertThat(opt4).isPresent();
                            assertThat(opt4.get()).isEmpty();
                        }
                );
    }

}