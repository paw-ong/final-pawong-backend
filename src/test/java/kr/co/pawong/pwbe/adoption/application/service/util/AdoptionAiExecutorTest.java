package kr.co.pawong.pwbe.adoption.application.service.util;

import kr.co.pawong.pwbe.infrastructure.ai.AdoptionAiExecutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class AdoptionAiExecutorTest {

    private ExecutorService executorService;
    private AdoptionAiExecutor aiExecutor;

    @BeforeEach
    void setUp() {
        executorService = Executors.newFixedThreadPool(2);
        aiExecutor = new AdoptionAiExecutor(executorService);
    }

    @AfterEach
    void tearDown() {
        executorService.shutdownNow();
    }

    @Test
    void 모든_INPUT_성공_시에_반환값이_리스트에_순서대로_들어있어야_함() {
        // Given
        List<Integer> inputs = List.of(1, 2, 3);
        Function<Integer, Integer> square = x -> x * x;
        // When
        List<Optional<Integer>> results = aiExecutor.run(inputs, square);
        // Then
        assertThat(results)
                .hasSize(3)
                .containsExactly(
                        Optional.of(1),
                        Optional.of(4),
                        Optional.of(9)
                );
    }

    @Test
    void 예외가_발생한_항목은_Optional_Empty_가_들어있어야_함() {
        // Given
        List<String> inputs = List.of("ok", "boom", "ok2");
        Function<String, String> fn = s -> {
            if (s.equals("boom")) {
                throw new IllegalStateException("fail on boom");
            }
            return s.toUpperCase();
        };
        // When
        List<Optional<String>> results = aiExecutor.run(inputs, fn);
        // Then
        assertThat(results)
                .hasSize(3)
                .satisfiesExactly(
                        opt0 -> assertThat(opt0).contains("OK"),
                        opt1 -> assertThat(opt1).isEmpty(),
                        opt2 -> assertThat(opt2).contains("OK2")
                );
    }

    @Test
    void 함수_결과값이_null_인_항목은_Optional_Empty_가_들어있어야_함() {
        // Given
        List<String> inputs = List.of("a", "b", "c");
        Function<String, String> fn = s -> s.equals("b") ? null : s;
        // When
        List<Optional<String>> results = aiExecutor.run(inputs, fn);
        // Then
        assertThat(results)
                .hasSize(3)
                .containsExactly(
                        Optional.of("a"),
                        Optional.empty(),
                        Optional.of("c")
                );
    }

    @Test
    void 함수_결과값이_null_인_경우와_예외가_발생한_경우가_다_있는_경우() {
        // Given
        List<String> inputs = List.of("ok", "boom", "ok2", "b", "boom", "ok3");
        Function<String, String> fn = s -> {
            if (s.equals("boom")) {
                throw new IllegalStateException("fail on boom");
            } else if (s.equals("b")) {
                return null;
            }
            return s.toUpperCase();
        };
        // When
        List<Optional<String>> results = aiExecutor.run(inputs, fn);
        // Then
        assertThat(results)
                .hasSize(6)
                .satisfiesExactly(
                        opt0 -> assertThat(opt0).contains("OK"),
                        opt1 -> assertThat(opt1).isEmpty(),
                        opt2 -> assertThat(opt2).contains("OK2"),
                        opt3 -> assertThat(opt3).isEmpty(),
                        opt4 -> assertThat(opt4).isEmpty(),
                        opt5 -> assertThat(opt5).contains("OK3")
                );
    }
}