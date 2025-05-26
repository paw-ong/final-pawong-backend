package kr.co.pawong.pwbe.infrastructure.fcm.application.service.support;

import kr.co.pawong.pwbe.infrastructure.fcm.domain.FcmToken;
import kr.co.pawong.pwbe.infrastructure.fcm.adapter.persistence.jpa.entity.FcmTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class FcmMapper {

    // FcmTokenEntity -> FcmToken
    public FcmToken toDomain(FcmTokenEntity entity) {
        return FcmToken.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .token(entity.getToken())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

    }

    // FcmToken -> FcmTokenEntity
    public FcmTokenEntity toEntity(FcmToken fcmToken) {
        return FcmTokenEntity.builder()
                .id(fcmToken.getId())
                .userId(fcmToken.getUserId())
                .token(fcmToken.getToken())
                .createdAt(fcmToken.getCreatedAt())
                .updatedAt(fcmToken.getUpdatedAt())
                .build();
    }
}
