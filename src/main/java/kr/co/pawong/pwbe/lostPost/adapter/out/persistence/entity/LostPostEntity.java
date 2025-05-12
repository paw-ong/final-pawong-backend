package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostStatus;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "LostPosts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "lost_post_seq",
        sequenceName = "lost_post_seq",
        allocationSize = 1              // id 1씩 증가
)
public class LostPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lost_post_seq")
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
    private String imageUrl;        // 이미지 url

    @Column(nullable = false)
    private String specialMark;     // 동물 특징

    private String content;         // 상세 내용

    private String rfidCd;            // 마이크로 칩번호

    @Column(nullable = false)
    private LocalDate createdAt;    // 생성날짜
    private LocalDate updatedAt;    // 수정날짜
    private LocalDate deletedAt;    // 삭제날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;      // 게시글 상태

    @Column(nullable = false)
    private String location;        // 실종장소, 발견장소

    private Double latitude;        // 위도
    private Double longitude;       // 경도

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
                .imageUrl(lostPost.getImageUrl())
                .specialMark(lostPost.getSpecialMark())
                .content(lostPost.getContent())
                .rfidCd(lostPost.getRfidCd())
                .createdAt(lostPost.getCreatedAt())
                .updatedAt(lostPost.getUpdatedAt())
                .deletedAt(lostPost.getDeletedAt())
                .status(lostPost.getStatus())
                .location(lostPost.getLocation())
                .latitude(lostPost.getLatitude())
                .longitude(lostPost.getLongitude())
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
                .imageUrl(this.imageUrl)
                .specialMark(this.specialMark)
                .content(this.content)
                .rfidCd(this.rfidCd)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .deletedAt(this.deletedAt)
                .status(this.status)
                .location(this.location)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .userId(this.userId)
                .build();
    }
}
