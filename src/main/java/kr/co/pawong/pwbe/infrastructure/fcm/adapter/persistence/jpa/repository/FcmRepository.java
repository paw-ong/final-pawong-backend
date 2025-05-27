package kr.co.pawong.pwbe.infrastructure.fcm.adapter.persistence.jpa.repository;

import java.util.Optional;
import kr.co.pawong.pwbe.infrastructure.fcm.adapter.persistence.jpa.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmRepository extends JpaRepository<FcmTokenEntity, Long> {
    Optional<FcmTokenEntity> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
