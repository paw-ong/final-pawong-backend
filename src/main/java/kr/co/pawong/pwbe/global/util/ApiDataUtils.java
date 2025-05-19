package kr.co.pawong.pwbe.global.util;

import groovy.util.logging.Slf4j;

@Slf4j
public class ApiDataUtils {

    // 문자열 -> 정수 (나이)
    public static Integer parseIntAge(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            String age = value.substring(0, 4);
            return Integer.parseInt(age);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // 문자열 -> Integer
   public static Integer parseInt(String value, Integer defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // 문자열 -> double
    public static Double parseDouble(String value, Double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // 문자열 -> Enum
    public static <T extends Enum<T>> T convertToEnum(String data, Class<T> enumClass) {
        if (data == null) {
            return null;
        }

        try {
            return Enum.valueOf(enumClass, data);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // 주소 -> 시도, 시군구
    public static String[] parseAddress(String careAddr) {
        String[] result = new String[2]; // [0] = city, [1] = district

        if (careAddr != null && !careAddr.isEmpty()) {
            String[] parts = careAddr.split(" ");
            if (parts.length >= 2) {
                result[0] = parts[0]; // city
                result[1] = parts[1]; // district
            }
        }

        return result;
    }
}
