package kr.co.pawong.pwbe.infrastructure.ai.enums;

public enum RefineMsg {
    REFINE_TEMPLATE_1("""
                        입력 문장: %s
                        
                        입력 문장에서 오타를 수정해주고 유기 동물과 관련된 정보만 남기고 나머지는 제거해줘.
                        그외에는 다른말 하지마.
                        """);

    private String template;

    RefineMsg(String template) {
        this.template = template;
    }

    public String getMessage(String searchTerm) {
        return this.template.formatted(searchTerm);
    }
}
