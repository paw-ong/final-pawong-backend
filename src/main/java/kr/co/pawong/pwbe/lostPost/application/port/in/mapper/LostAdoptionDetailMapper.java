package kr.co.pawong.pwbe.lostPost.application.port.in.mapper;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailDto;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;

public class LostAdoptionDetailMapper {

    public static LostAdoptionDetailDto toModel(LostAdoption lostAdoption, String careNm) {

        return LostAdoptionDetailDto.builder()
                .adoptionId(lostAdoption.getAdoptionId())
                .noticeSdt(lostAdoption.getNoticeSdt())
                .noticeEdt(lostAdoption.getNoticeEdt())
                .updTm(lostAdoption.getUpdTm())
                .upKindNm(lostAdoption.getUpKindNm())
                .kindNm(lostAdoption.getKindNm())
                .colorCd(lostAdoption.getColorCd())
                .sexCd(lostAdoption.getSexCd())
                .age(lostAdoption.getAge())
                .popfile1(lostAdoption.getPopfile1())
                .popfile2(lostAdoption.getPopfile2())
                .specialMark(lostAdoption.getSpecialMark())
                .happenPlace(lostAdoption.getHappenPlace())
                .neuterYn(lostAdoption.getNeuterYn())
                .careNm(careNm)
                .build();
    }

}
