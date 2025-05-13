package kr.co.pawong.pwbe.global.time;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeUtilsTest {

    private TimeUtils utils;
    private static final LocalDateTime FIXED_LDT =
            LocalDateTime.of(2025, 5, 13, 12, 0, 0);
    private static final LocalDate FIXED_LD =
            LocalDate.of(2025, 5, 13);

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(FIXED_LDT.atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault());
        utils = new TimeUtils(fixedClock);
    }

    @Test
    void 시간_포맷팅_초_테스트() {
        LocalDateTime fiveSecondsAgo = FIXED_LDT.minusSeconds(5);
        assertThat(utils.formatTimeAgo(fiveSecondsAgo))
                .isEqualTo("5초 전");
    }

    @Test
    void 시간_포맷팅_분_테스트() {
        LocalDateTime tenMinutesAgo = FIXED_LDT.minusMinutes(10);
        assertThat(utils.formatTimeAgo(tenMinutesAgo))
                .isEqualTo("10분 전");
    }

    @Test
    void 시간_포맷팅_년_테스트() {
        LocalDateTime twoYearsAgo = FIXED_LDT.minusYears(2);
        assertThat(utils.formatTimeAgo(twoYearsAgo))
                .isEqualTo("2년 전");
    }


    @Test
    void 날_수_포맷팅_당일_테스트() {
        LocalDate today = FIXED_LD;
        assertThat(utils.formatDateAgo(today))
                .isEqualTo("오늘");
    }

    @Test
    void 날_수_포맷팅_일_테스트() {
        LocalDate fiveDaysAgo = FIXED_LD.minusDays(5);
        assertThat(utils.formatDateAgo(fiveDaysAgo))
                .isEqualTo("5일 전");
    }

    @Test
    void 날_수_포맷팅_월_테스트() {
        LocalDate threeMonthsAgo = FIXED_LD.minusMonths(3);
        assertThat(utils.formatDateAgo(threeMonthsAgo))
                .isEqualTo("3개월 전");
    }

    @Test
    void 날_수_포맷팅_년_테스트() {
        LocalDate twoYearsAgo = FIXED_LD.minusYears(2);
        assertThat(utils.formatDateAgo(twoYearsAgo))
                .isEqualTo("2년 전");
    }

}