package kr.co.pawong.pwbe.global.util;

import java.security.SecureRandom;

public class CodeGenerator {

    // 보안에 강한 랜덤 숫자 생성
    private static final SecureRandom RNG = new SecureRandom();

    public static String generateCode() {
        int num = RNG.nextInt(1_000_000); // 0부터 999,999 사이의 정수
        return String.format("%06d", num); // 항상 6자리 문자열로 반환 (앞에 0 채움)
    }

}
