package utils;

import java.time.LocalDateTime;

public class FileUtils {
    public static boolean isValidDateTime(int year, int month, int day, int hour, int minute, int second) {
        return year >= 1900 && year <= 2100 &&
                month >= 1 && month <= 12 &&
                day >= 1 && day <= 31 &&
                hour >= 0 && hour <= 23 &&
                minute >= 0 && minute <= 59 &&
                second >= 0 && second <= 59;
    }

    public static LocalDateTime createDateTime(int year, int month, int day, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }
}

