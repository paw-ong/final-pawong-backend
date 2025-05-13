package kr.co.pawong.pwbe.user.adapter.in.api;

import java.util.ArrayList;
import java.util.List;
import kr.co.pawong.pwbe.user.adapter.in.api.dto.response.BaseMyPageResponse;
import kr.co.pawong.pwbe.user.adapter.in.api.dto.response.MyPageLostPost;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
public class MyPageController {

    @GetMapping("/lost-posts")
    public ResponseEntity<BaseMyPageResponse<MyPageLostPost>> myPageLostPosts() {

        return ResponseEntity.ok(new BaseMyPageResponse<>(new ArrayList<>()));
    }
}
