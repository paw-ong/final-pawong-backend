package kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto;

import java.net.URL;

public record ChatRoomLostPostInfo(
        Long postId,        // 실종 공고id
        String kindNm,    // 품종명
        String location,    // 장소
        String author,      // 글 작성자
        Long authorId,
        URL imageUrl     // 이미지url
) {

}