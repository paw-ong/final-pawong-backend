package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import kr.co.pawong.pwbe.lostPost.domain.LostGeoPoint;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class GeoPointEmbeddable {

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    public GeoPointEmbeddable(LostGeoPoint loc) {
        this.latitude = loc.getLatitude();
        this.longitude = loc.getLongitude();
    }

    public LostGeoPoint toDomain() {
        return new LostGeoPoint(latitude, longitude);
    }

}
