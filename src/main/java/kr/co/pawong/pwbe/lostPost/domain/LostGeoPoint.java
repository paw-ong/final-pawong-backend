package kr.co.pawong.pwbe.lostPost.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LostGeoPoint {

    private final BigDecimal latitude;      // 위도
    private final BigDecimal longitude;     // 경도

    public static LostGeoPoint of(double lat, double lng) {
        return new LostGeoPoint(
                BigDecimal.valueOf(lat),
                BigDecimal.valueOf(lng)
        );
    }

}
