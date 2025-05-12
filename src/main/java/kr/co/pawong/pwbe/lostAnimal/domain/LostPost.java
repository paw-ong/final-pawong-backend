package kr.co.pawong.pwbe.lostAnimal.domain;

import java.time.LocalDate;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import kr.co.pawong.pwbe.lostAnimal.enums.PostStatus;
import kr.co.pawong.pwbe.lostAnimal.enums.PostType;
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
    private int age;                // 나이
    private String imageUrl;        // 이미지 url
    private String specialMark;     // 동물 특징
    private String content;         // 상세 내용
    private Long rfidCd;            // 마이크로 칩번호
    private LocalDate createdAt;    // 생성날짜
    private LocalDate updatedAt;    // 수정날짜
    private LocalDate deletedAt;    // 삭제날짜
    private PostStatus status;      // 게시글 상태
    private String location;        // 실종장소, 발견장소
    private Double latitude;        // 위도
    private Double longitude;       // 경도
    private Long userId;            // 작성자 유저 id
}
