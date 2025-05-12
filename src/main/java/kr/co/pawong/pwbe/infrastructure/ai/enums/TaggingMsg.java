package kr.co.pawong.pwbe.infrastructure.ai.enums;

public enum TaggingMsg {
    TAGGING_TEMPLATE_1("""
            동물 특징: %s
            
            위의 동물 특징을 기반으로 적절한 키워드만 선택해서 반환해줘.
            아래 키워드 중에 정말 적합한 것만 한 줄로 반환하고, 그 외에는 아무 말도 하지마.
            선택한 키워드는 공백을 기준으로 구분해서 나열해줘.
            (참고-나이는 1살 이하면 어린거고, 7살 이상이면 많은것임. 무게는 5kg 이하면 가벼운고, 15kg 이상이면 무거운것임.)
            
            키워드: %s
            """);

    private String template;

    TaggingMsg(String template) {
        this.template = template;
    }

    public String getMessage(String feature) {
        return this.template.formatted(feature, AnimalFeature.getAllFeaturesAsString());
    }
}
