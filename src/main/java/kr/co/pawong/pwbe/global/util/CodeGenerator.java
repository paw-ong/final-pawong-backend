package kr.co.pawong.pwbe.global.util;

import java.security.SecureRandom;

public class CodeGenerator {

    // 보안에 강한 랜덤 숫자 생성
    private static final SecureRandom RNG = new SecureRandom();

    public static String generateCode() {
        int num;
        do {
            num = RNG.nextInt(1_000_000);
        } while (num == 0);
        return String.format("%06d", num);
    }

}
