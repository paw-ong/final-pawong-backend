package kr.co.pawong.pwbe.infrastructure.ai.enums;

import org.junit.jupiter.api.Test;

import static kr.co.pawong.pwbe.infrastructure.ai.enums.TaggingMsg.TAGGING_TEMPLATE_1;

class TaggingMsgTest {

    @Test
    void AI_시스템_메시지_확인() {
        System.out.println(TAGGING_TEMPLATE_1.getMessage("아아아아"));
    }
}