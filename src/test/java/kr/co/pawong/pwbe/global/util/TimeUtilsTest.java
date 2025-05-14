package kr.co.pawong.pwbe.global.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeUtilsTest {

    private Clock fixedClock;
    private static final LocalDateTime FIXED_LDT =
            LocalDateTime.of(2025, 5, 13, 12, 14, 3);
    private static final LocalDate FIXED_LD =
            LocalDate.of(2025, 5, 13);

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(FIXED_LDT.atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault());
    }

    @Test
    void 시간_포맷팅_테스트() {
        // given
        LocalDateTime dt = LocalDateTime.of(2025, 5, 13, 12, 12, 2);
        // when
        String strTime = TimeUtils.formatTime(dt);
        // then
        assertThat(strTime)
                .isEqualTo("2025-05-13 12:12:02");
    }

    @Test
    void 시간_포맷팅_null_입력_시_공백_반환() {
        // given
        LocalDateTime dt = null;
        // when
        String strTime = TimeUtils.formatTime(dt);
        // then
        assertThat(strTime)
                .isEqualTo("");
    }

    @Test
    void 날짜_포맷팅_테스트() {
        // given
        LocalDate dt = LocalDate.of(2025, 5, 13);
        // when
        String strTime = TimeUtils.formatDate(dt);
        // then
        assertThat(strTime)
                .isEqualTo("2025-05-13");
    }

    @Test
    void 날짜_포맷팅_null_입력_시_공백_반환() {
        // given
        LocalDate dt = null;
        // when
        String strTime = TimeUtils.formatDate(dt);
        // then
        assertThat(strTime)
                .isEqualTo("");
    }

    @Test
    void 시간_한글_포맷팅_초_테스트() {
        LocalDateTime fiveSecondsAgo = FIXED_LDT.minusSeconds(5);
        assertThat(TimeUtils.formatTimeAgo(fiveSecondsAgo, fixedClock))
                .isEqualTo("5초 전");
    }

    @Test
    void 시간_한글_포맷팅_분_테스트() {
        LocalDateTime tenMinutesAgo = FIXED_LDT.minusMinutes(10);
        assertThat(TimeUtils.formatTimeAgo(tenMinutesAgo, fixedClock))
                .isEqualTo("10분 전");
    }

    @Test
    void 시간_한글_포맷팅_년_테스트() {
        LocalDateTime twoYearsAgo = FIXED_LDT.minusYears(2);
        assertThat(TimeUtils.formatTimeAgo(twoYearsAgo, fixedClock))
                .isEqualTo("2년 전");
    }


    @Test
    void 날_수_한글_포맷팅_당일_테스트() {
        LocalDate today = FIXED_LD;
        assertThat(TimeUtils.formatDateAgo(today, fixedClock))
                .isEqualTo("오늘");
    }

    @Test
    void 날_수_한글_포맷팅_일_테스트() {
        LocalDate fiveDaysAgo = FIXED_LD.minusDays(5);
        assertThat(TimeUtils.formatDateAgo(fiveDaysAgo, fixedClock))
                .isEqualTo("5일 전");
    }

    @Test
    void 날_수_한글_포맷팅_월_테스트() {
        LocalDate threeMonthsAgo = FIXED_LD.minusMonths(3);
        assertThat(TimeUtils.formatDateAgo(threeMonthsAgo, fixedClock))
                .isEqualTo("3개월 전");
    }

    @Test
    void 날_수_한글_포맷팅_년_테스트() {
        LocalDate twoYearsAgo = FIXED_LD.minusYears(2);
        assertThat(TimeUtils.formatDateAgo(twoYearsAgo, fixedClock))
                .isEqualTo("2년 전");
    }

}