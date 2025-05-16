package kr.co.pawong.pwbe.user.adapter.out.jpa.repository;

import java.util.Optional;
import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findBySocialId(Long socialId);
  Optional<UserEntity> findByUserId(Long userId);
}
