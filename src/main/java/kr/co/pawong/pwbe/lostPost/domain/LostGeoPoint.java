package kr.co.pawong.pwbe.lostPost.domain;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.LOCATION_REQUEST_ERROR;

import java.math.BigDecimal;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LostGeoPoint {

    private final BigDecimal latitude;      // 위도
    private final BigDecimal longitude;     // 경도

    public static LostGeoPoint of(Double lat, Double lng) {
        try {
            return new LostGeoPoint(
                    BigDecimal.valueOf(lat),
                    BigDecimal.valueOf(lng)
            );
        } catch (Exception exception) {
            throw new BaseException(LOCATION_REQUEST_ERROR);
        }

    }

}
