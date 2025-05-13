package kr.co.pawong.pwbe.global.time;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeUtils {

    private final Clock clock;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 시간을 yyyy-MM-dd HH:mm:ss 형식의 문자열로 반환합니다.
     */
    public static String formatTime(LocalDateTime time) {
        if (time == null) {
            return "";
        }
        return time.format(DATETIME_FORMATTER);
    }

    /**
     * 날짜를 yyyy-MM-dd 형식의 문자열로 반환합니다.
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * 현재로부터 어느 정도 이전인지 한글로 반환합니다.
     * @param past 비교 시간 - LocalDateTime
     */
    public String formatTimeAgo(LocalDateTime past) {
        if (past == null) {
            return "";
        }
        LocalDateTime now = LocalDateTime.now(clock);
        long years   = ChronoUnit.YEARS.between(past, now);
        if (years > 0) return years + "년 전";
        long months  = ChronoUnit.MONTHS.between(past, now);
        if (months > 0) return months + "개월 전";
        long days    = ChronoUnit.DAYS.between(past, now);
        if (days > 0) return days + "일 전";
        long hours   = ChronoUnit.HOURS.between(past, now);
        if (hours > 0) return hours + "시간 전";
        long minutes = ChronoUnit.MINUTES.between(past, now);
        if (minutes > 0) return minutes + "분 전";
        long seconds = ChronoUnit.SECONDS.between(past, now);
        return seconds + "초 전";
    }

    /**
     * 현재로부터 어느 정도 이전인지 한글로 반환합니다.
     * @param past 비교 시간 - LocalDate
     */
    public String formatDateAgo(LocalDate past) {
        if (past == null) {
            return "";
        }
        LocalDate today = LocalDate.now(clock);
        long years  = ChronoUnit.YEARS.between(past, today);
        if (years > 0) return years + "년 전";
        long months = ChronoUnit.MONTHS.between(past, today);
        if (months > 0) return months + "개월 전";
        long days   = ChronoUnit.DAYS.between(past, today);
        if (days > 0) return days + "일 전";
        return "오늘";
    }
}
