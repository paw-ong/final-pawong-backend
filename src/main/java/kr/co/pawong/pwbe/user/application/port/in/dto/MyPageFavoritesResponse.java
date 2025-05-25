package kr.co.pawong.pwbe.user.application.port.in.dto;

import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// MyPage에서 찜 목록 반환 객체로 활용
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageFavoritesResponse {

    private AdoptionCard adoptionCard;
}