package kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto;

import java.net.URL;

public record ChatRoomLostPostInfo(
        String location,    // 장소
        String author,      // 글 작성자
        URL imageUrl     // 이미지url
) {

}