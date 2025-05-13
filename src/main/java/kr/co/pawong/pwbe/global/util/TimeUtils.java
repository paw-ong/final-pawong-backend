package kr.co.pawong.pwbe.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
    /**
     * 주어진 날짜가 지금으로부터 얼마나 전에 발생했는지를
     * "몇 년 전", "몇 달 전", ... "몇 초 전" 형식으로 반환합니다.
     *
     * @param pastDateTime 과거 시점
     * @return 경과 시간 문자열
     */
    public static String toTimeAgo(LocalDateTime pastDateTime) {
        LocalDateTime now = LocalDateTime.now();

        long years = ChronoUnit.YEARS.between(pastDateTime, now);
        if (years > 0) {
            return years + "년 전";
        }

        long months = ChronoUnit.MONTHS.between(pastDateTime, now);
        if (months > 0) {
            return months + "개월 전";
        }

        long days = ChronoUnit.DAYS.between(pastDateTime, now);
        if (days > 0) {
            return days + "일 전";
        }

        long hours = ChronoUnit.HOURS.between(pastDateTime, now);
        if (hours > 0) {
            return hours + "시간 전";
        }

        long minutes = ChronoUnit.MINUTES.between(pastDateTime, now);
        if (minutes > 0) {
            return minutes + "분 전";
        }

        long seconds = ChronoUnit.SECONDS.between(pastDateTime, now);
        return seconds + "초 전";
    }

    // LocalDate 버전

    /**
     * LocalDate 버전
     */
    public static String toDateAgo(LocalDate pastDate) {
        LocalDate today = LocalDate.now();

        long years = ChronoUnit.YEARS.between(pastDate, today);
        if (years > 0) {
            return years + "년 전";
        }

        long months = ChronoUnit.MONTHS.between(pastDate, today);
        if (months > 0) {
            return months + "개월 전";
        }

        long days = ChronoUnit.DAYS.between(pastDate, today);
        if (days > 0) {
            return days + "일 전";
        }

        return "오늘";
    }
}
