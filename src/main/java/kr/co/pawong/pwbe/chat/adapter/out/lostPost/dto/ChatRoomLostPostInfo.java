package kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto;

public record ChatRoomLostPostInfo(
        String location,    // 장소
        String author,      // 글 작성자
        String imageUrl     // 이미지url
) {

}