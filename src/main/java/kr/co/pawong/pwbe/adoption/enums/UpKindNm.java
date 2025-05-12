package kr.co.pawong.pwbe.adoption.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum UpKindNm {
    개("417000"),
    고양이("422400"),
    기타("429900");

    private final String value;
    UpKindNm(String value) {
        this.value = value;
    }
}
