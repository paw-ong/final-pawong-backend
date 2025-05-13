package kr.co.pawong.pwbe.user.adapter.out.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.co.pawong.pwbe.user.domain.User;
import kr.co.pawong.pwbe.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")   // User가 예약어라 Users로 만들었습니다.
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;                  // 사용자 ID
  private Long socialId;            // 소셜 ID
  private String nickname;          // 닉네임
  private String profileImage;      // 프로필 이미지
  private String region;            // 지역
  private String tel;               // 전화번호
  @Enumerated(EnumType.STRING)
  private UserStatus status;        // 사용자 상태
  private LocalDateTime createdAt;      // 가입날짜
  private LocalDateTime updatedAt;      // 수정날짜
  private LocalDateTime deletedAt;      // 삭제날짜

  public User toDomain() {
    return User.builder()
        .userId(userId)
        .socialId(socialId)
        .nickname(nickname)
        .profileImage(profileImage)
        .region(region)
        .tel(tel)
        .status(status)
        .createdAt(createdAt)
        .updatedAt(updatedAt)
        .deletedAt(deletedAt)
        .build();
  }

  public static UserEntity of(User user) {
    return UserEntity.builder()
        .userId(user.getUserId())
        .socialId(user.getSocialId())
        .nickname(user.getNickname())
        .profileImage(user.getProfileImage())
        .region(user.getRegion())
        .tel(user.getTel())
        .status(user.getStatus())
        .createdAt(LocalDateTime.now())
        .updatedAt(user.getUpdatedAt())
        .deletedAt(user.getDeletedAt())
        .build();
  }

  public UserEntity updateProfile(User user) {
    this.nickname = user.getNickname();
    this.region = user.getRegion();
    this.tel = user.getTel();
    this.updatedAt = LocalDateTime.now();
    this.status = user.getStatus();
    return this;
  }

}
