package kr.co.pawong.pwbe.lostPost.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GeoPoint {

    private final BigDecimal latitude;      // 위도
    private final BigDecimal longitude;     // 경도

    public static GeoPoint of(double lat, double lng) {
        return new GeoPoint(
                BigDecimal.valueOf(lat),
                BigDecimal.valueOf(lng)
        );
    }

}
