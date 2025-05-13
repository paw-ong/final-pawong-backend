package kr.co.pawong.pwbe.global.time;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class TimeUtils {

    private final Clock clock;

    public TimeUtils(Clock clock) {
        this.clock = clock;
    }

    public String formatTimeAgo(LocalDateTime past) {
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

    public String formatDateAgo(LocalDate past) {
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
