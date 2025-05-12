package kr.co.pawong.pwbe.adoption.application.port.out;

import java.util.List;
import java.util.Optional;

public interface AdoptionAiPort {

    // 문장 정제하는 함수
    String refineSpecialMark(String specialMark);

    // 입력된 문장에 대해 태그를 선택해서 문자열 리스트로 반환하는 함수
    List<String> tag(String feature);

    // 문장 임베딩하는 함수
    float[] embed(String completion);

    // 여러 개 문장을 병렬로 정제해서 순서대로 반환
    List<Optional<String>> refineSpecialMarkBatch(List<String> specialMarksg);

    //여러 개 feature를 병렬로 태깅해서 순서대로 반환
    List<Optional<List<String>>> tagBatch(List<String> features);

    // 여러 개 문장에 대해 병렬로 임베딩해서 순서대로 반환
    List<Optional<float[]>> embedBatch(List<String> completions);
}
