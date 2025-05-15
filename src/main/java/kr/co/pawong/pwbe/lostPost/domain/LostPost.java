package kr.co.pawong.pwbe.lostPost.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import kr.co.pawong.pwbe.lostPost.enums.PostStatus;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LostPost {

    private Long lostPostId;        // 실종게시글 id
    private PostType postType;      // 실종(LOST), 발견(FOUND), 보호(FOSTER)
    private LocalDate date;         // 실종날짜, 발견날짜
    private UpKindNm upKindNm;      // 축종명
    private UpKindCd upKindCd;      // 축종코드
    private String kindNm;          // 품종명
    private String color;           // 색상
    private SexCd sexCd;            // 성별
    private Integer age;                // 나이
    private String imageUrl;        // 이미지 url
    private String specialMark;     // 동물 특징
    private String content;         // 상세 내용
    private String rfidCd;            // 마이크로 칩번호
    private LocalDateTime createdAt;    // 생성날짜
    private LocalDateTime updatedAt;    // 수정날짜
    private LocalDateTime deletedAt;    // 삭제날짜
    private PostStatus status;      // 게시글 상태
    private String location;        // 실종장소, 발견장소
    private GeoPoint geoPoint;
    private Long userId;            // 작성자 유저 id

    public void createBy(Long userId) {
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.status = PostStatus.ACTIVE;
    }
}
