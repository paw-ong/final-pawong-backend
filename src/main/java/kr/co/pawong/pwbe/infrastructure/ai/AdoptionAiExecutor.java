package kr.co.pawong.pwbe.infrastructure.ai;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * global 에서 등록한 Executor 를 사용해서 병렬 처리를 구현합니다.
 */
@Component
public class AdoptionAiExecutor {
    private final Executor executor;

    public AdoptionAiExecutor(@Qualifier("aiExecutor") Executor executor) {
        this.executor = executor;
    }

    /**
     * inputs 의 각 요소에 fn.apply 를 병렬로 수행하고,
     * 모든 작업이 끝난 뒤 결과 리스트를 돌려줍니다.
     * @param inputs - 이 순서와 결과물의 순서가 같습니다.
     * @return 반환 리스트에서 Optional `empty`인 항목은 결과가 `null`이거나 예외가 발생한 항목입니다.
     */
    public <T, R> List<Optional<R>> run(List<T> inputs, Function<T, R> fn) {
        // 각 입력마다 CompletableFuture 생성
        List<CompletableFuture<Optional<R>>> futures = inputs.stream()
                .map(t -> CompletableFuture
                        .supplyAsync(() -> Optional.ofNullable(fn.apply(t)), executor)
                        .exceptionally(ex -> Optional.empty()))
                .toList();

        // 모든 Future 가 완료될 때까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 결과 수집
        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }
}
