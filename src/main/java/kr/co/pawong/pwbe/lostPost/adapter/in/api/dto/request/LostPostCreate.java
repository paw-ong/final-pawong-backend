package kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.request;

import java.time.LocalDate;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LostPostCreate {

    private PostType postType;      // 실종(LOST), 발견(FOUND), 보호(FOSTER)
    private LocalDate date;         // 실종날짜, 발견날짜
    private UpKindNm upKindNm;      // 축종명
    private String kindNm;          // 품종명
    private String color;           // 색상
    private SexCd sexCd;            // 성별
    private Integer age;            // 나이
    private String imageUrl;        // 이미지 url
    private String specialMark;     // 동물 특징
    private String content;         // 상세 내용
    private String rfidCd;            // 마이크로 칩번호
    private String location;        // 실종장소, 발견장소
    private Double latitude;        // 위도
    private Double longitude;       // 경도

    public LostPost toDomain() {
        return LostPost.builder()
                .postType(this.postType)
                .date(this.date)
                .upKindNm(this.upKindNm)
                .upKindCd(UpKindCd.fromValue(this.upKindNm.getValue()))
                .kindNm(this.kindNm)
                .color(this.color)
                .sexCd(this.sexCd)
                .age(this.age)
                .imageUrl(this.imageUrl)
                .specialMark(this.specialMark)
                .content(this.content)
                .rfidCd(this.rfidCd)
                .location(this.location)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }
}
