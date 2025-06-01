package kr.co.pawong.pwbe.lostPost.application.port.in.mapper;

import java.net.URL;
import java.time.Clock;
import kr.co.pawong.pwbe.global.util.TimeUtils;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailDto;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;

public class LostPostDetailMapper {

    public static LostPostDetailDto toModel(LostPost lostPost, String author, boolean bookmarked, Clock clock, URL url) {

        return LostPostDetailDto.builder()
                .lostPostId(lostPost.getLostPostId())
                .postType(lostPost.getPostType())
                .date(TimeUtils.formatDate(lostPost.getDate()))
                .upKindNm(lostPost.getUpKindNm())
                .kindNm(lostPost.getKindNm())
                .color(lostPost.getColor())
                .sexCd(lostPost.getSexCd())
                .age(lostPost.getAge())
                .imageUrl(url)
                .specialMark(lostPost.getSpecialMark())
                .content(lostPost.getContent())
                .rfidCd(lostPost.getRfidCd())
                .createdAt(lostPost.getCreatedAt())
                .updatedAt(lostPost.getUpdatedAt())
                .deletedAt(lostPost.getDeletedAt())
                .status(lostPost.getStatus())
                .location(lostPost.getLocation())
                .lostGeoPoint(lostPost.getLostGeoPoint())
                .author(author)
                .authorId(lostPost.getUserId())
                .bookmarked(bookmarked)
                .build();
    }
}
