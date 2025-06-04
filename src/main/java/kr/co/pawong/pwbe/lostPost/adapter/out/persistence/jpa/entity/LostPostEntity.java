package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FORBIDDEN_POST_MODIFY;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostStatus;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "LostPosts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("status = 'ACTIVE'")
public class LostPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lostPostId;        // 실종게시글 id

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostType postType;      // 실종(LOST), 발견(FOUND), 보호(FOSTER)

    @Column(nullable = false)
    private LocalDate date;         // 실종날짜, 발견날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UpKindNm upKindNm;      // 축종명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UpKindCd upKindCd;      // 축종코드

    private String kindNm;          // 품종명

    @Column(nullable = false)
    private String color;           // 색상

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SexCd sexCd;            // 성별

    private Integer age;            // 나이

    @Column(columnDefinition = "TEXT")
    private String imageKey;        // 이미지 Object key

    @Column(nullable = false)
    private String specialMark;     // 동물 특징

    private String content;         // 상세 내용

    private String rfidCd;            // 마이크로 칩번호

    @Column(nullable = false)
    private LocalDateTime createdAt;    // 생성날짜
    private LocalDateTime updatedAt;    // 수정날짜
    private LocalDateTime deletedAt;    // 삭제날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;      // 게시글 상태

    @Column(nullable = false)
    private String location;        // 실종장소, 발견장소

    @Embedded
    private GeoPointEmbeddable geoPoint;

    private Long userId;            // 작성자 유저 id

    public static LostPostEntity from(LostPost lostPost) {
        return LostPostEntity.builder()
                .postType(lostPost.getPostType())
                .date(lostPost.getDate())
                .upKindNm(lostPost.getUpKindNm())
                .upKindCd(lostPost.getUpKindCd())
                .kindNm(lostPost.getKindNm())
                .color(lostPost.getColor())
                .sexCd(lostPost.getSexCd())
                .age(lostPost.getAge())
                .imageKey(lostPost.getImageKey())
                .specialMark(lostPost.getSpecialMark())
                .content(lostPost.getContent())
                .rfidCd(lostPost.getRfidCd())
                .createdAt(lostPost.getCreatedAt())
                .updatedAt(lostPost.getUpdatedAt())
                .deletedAt(lostPost.getDeletedAt())
                .status(lostPost.getStatus())
                .location(lostPost.getLocation())
                .geoPoint(new GeoPointEmbeddable(lostPost.getLostGeoPoint()))
                .userId(lostPost.getUserId())
                .build();
    }

    public LostPost toDomain() {
        return LostPost.builder()
                .lostPostId(this.lostPostId)
                .postType(this.postType)
                .date(this.date)
                .upKindNm(this.upKindNm)
                .upKindCd(this.upKindCd)
                .kindNm(this.kindNm)
                .color(this.color)
                .sexCd(this.sexCd)
                .age(this.age)
                .imageKey(this.imageKey)
                .specialMark(this.specialMark)
                .content(this.content)
                .rfidCd(this.rfidCd)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .deletedAt(this.deletedAt)
                .status(this.status)
                .location(this.location)
                .lostGeoPoint(this.geoPoint.toDomain())
                .userId(this.userId)
                .build();
    }

    public LostPostEntity updateBy(LostPost lostPost, Long userId) {
        if(!this.userId.equals(userId)) {
            throw new BaseException(FORBIDDEN_POST_MODIFY);
        }
        this.postType = lostPost.getPostType();
        this.date = lostPost.getDate();
        this.upKindNm = lostPost.getUpKindNm();
        this.upKindCd = lostPost.getUpKindCd();
        this.kindNm = lostPost.getKindNm();
        this.color = lostPost.getColor();
        this.sexCd = lostPost.getSexCd();
        this.age = lostPost.getAge();
        this.imageKey = lostPost.getImageKey();
        this.specialMark = lostPost.getSpecialMark();
        this.content = lostPost.getContent();
        this.rfidCd = lostPost.getRfidCd();
        this.location = lostPost.getLocation();
        this.geoPoint = new GeoPointEmbeddable(lostPost.getLostGeoPoint());
        this.updatedAt = LocalDateTime.now();
        this.status = PostStatus.ACTIVE;
        return this;
    }

    public void deleteBy(Long userId) {
        if(!this.userId.equals(userId)) {
            throw new BaseException(FORBIDDEN_POST_MODIFY);
        }
        this.deletedAt = LocalDateTime.now();
        this.status = PostStatus.DELETED;
    }

}
