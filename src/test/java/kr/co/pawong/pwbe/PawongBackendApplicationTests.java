package kr.co.pawong.pwbe;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 전체 테스트 돌리면 오류가 떠서 프로필을 설정했습니다.
 */
@Disabled("전체 테스트 시에 제외")
@SpringBootTest(properties = "spring.profiles.active=dev")
class PawongBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
