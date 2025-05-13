package kr.co.pawong.pwbe.user.application.port.in.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserCreate {
  private Long socialId;
  private String nickname;
  private String profileImage;

}
