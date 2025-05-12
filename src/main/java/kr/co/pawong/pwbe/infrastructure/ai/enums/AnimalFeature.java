package kr.co.pawong.pwbe.infrastructure.ai.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum AnimalFeature {
    색깔이밝음,
    색깔이어두움,
    색깔이섞임,
    구조됨,
    아이와잘지냄,
    노인과잘지냄,
    다른개와잘지냄,
    고양이와잘지냄,
    사람경계함,
    사람을잘따름,
    장시간혼자있을수있음,
    분리불안이없음,
    가정환경에적합함,
    실내생활적응됨,
    음식가림없음,
    산책을좋아함,
    입양경험있음,
    적응이빠름,
    건강함,
    질병있음,
    온순함,
    활발함,
    조용함,
    사회성이좋음,
    겁이많음,
    잘짖음,
    사나움,
    입질함,
    활동량많음,
    나이가어림,       // 대략 0~1살, 강아지/아기 고양이
    나이가중간임,     // 2~6살 정도, 성견/성묘
    나이가많음,       // 7살 이상, 노령
    무게가중간임,
    무게가가벼움, // 5kg 이하
    무게가무거움, // 15kg 이상
    소형견, // 10kg 미만 강아지
    중형견, // 10~25kg 미만 강아지
    대형견, // 25kg 이상 강아지
    훈련경험있음,
    이동장적응됨,
    차량탑승적응됨,
    장난감좋아함,
    소리에민감함,
    소리에둔감함,
    남성을더잘따름,
    여성을더잘따름,
    아이를싫어함,
    사람을좋아,
    알레르기가있음,
    집안파손경향있음,
    집안파손경향없음,
    장애가있음,
    장애가없음,
    출산경험있음,
    출산경험없음,
    예방접종이력있음,
    예방접종이력없음;

    // 모든 enum 상수를 하나의 문자열로 반환
    public static String getAllFeaturesAsString() {
        return Arrays.stream(AnimalFeature.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    /**
     * 문자열을 공백을 기준으로 자르고 유효한 태그만 선택해서 리스트로 반환함
     * @param tags - 동물 특징이 공백을 기준으로 나열된 문자열
     * @return 유효한 태그 문자열 리스트
     */
    public static List<String> extractValidTags(String tags) {
        String[] tagArr = tags.split(" ");
        List<String> result = new ArrayList<>();
        for (String tag : tagArr) {
            for (AnimalFeature animalFeature : AnimalFeature.values()) {
                if (animalFeature.name().equals(tag)) {
                    result.add(animalFeature.name());
                    break;
                }
            }
        }
        return result;
    }
}
