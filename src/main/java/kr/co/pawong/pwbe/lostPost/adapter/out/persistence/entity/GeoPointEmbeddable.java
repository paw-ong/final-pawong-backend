package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import kr.co.pawong.pwbe.lostPost.domain.GeoPoint;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class GeoPointEmbeddable {

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    public GeoPointEmbeddable(GeoPoint loc) {
        this.latitude  = loc.getLatitude();
        this.longitude = loc.getLongitude();
    }

    public GeoPoint toDomain() {
        return new GeoPoint(latitude, longitude);
    }

}
