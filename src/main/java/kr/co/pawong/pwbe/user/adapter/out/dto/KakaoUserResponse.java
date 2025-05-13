package kr.co.pawong.pwbe.user.adapter.out.dto;

import java.util.Map;
import kr.co.pawong.pwbe.user.application.port.in.dto.UserCreate;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Builder
@Data
public class KakaoUserResponse {
  public Long id;
  public String connected_at;
  public Properties properties;
  public KakaoAccount kakao_account;

  @Builder
  @Data
  public static class Properties {
    public String nickname;
    public String profile_image;
    public String thumbnail_image;
  }

  @Data
  public class KakaoAccount {
    public Boolean profile_nickname_needs_agreement;
    public Boolean profile_image_needs_agreement;
    public Profile profile;

    @Data
    public class Profile {
      public String nickname;
      public String thumbnail_image_url;
      public String profile_image_url;
      public Boolean is_default_image;
      public Boolean is_default_nickname;
    }
  }

  public UserCreate toUserCreate() {
    return UserCreate.builder()
        .socialId(id)
        .nickname(properties.nickname)
        .profileImage(properties.profile_image)
        .build();
  }

  public static KakaoUserResponse from(OAuth2User oAuth2User) {
    Map<String, Object> props = oAuth2User.getAttribute("properties");
    KakaoUserResponse.Properties properties = KakaoUserResponse.Properties.builder()
        .nickname(props.get("nickname").toString())
        .profile_image(props.get("profile_image").toString())
        .thumbnail_image(props.get("thumbnail_image").toString())
        .build();

    return KakaoUserResponse.builder()
        .id(oAuth2User.getAttribute("id"))
        .connected_at(oAuth2User.getAttribute("connected_at"))
        .properties(properties)
        .build();
  }
}
