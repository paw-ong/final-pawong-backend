package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.repository.LostPostJpaRepository;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLostPostDataQueryAdapter implements LostPostDataQueryPort {

    private final LostPostJpaRepository lostPostJpaRepository;

    @Override
    public List<LostPost> getLostPostsByUserId(Long userId) {
        return lostPostJpaRepository.findByUserId(userId).stream()
                .map(LostPostEntity::toDomain)
                .toList();
    }

    @Override
    public List<LostPost> getLostPostsByPostType(PostType postType){
        return lostPostJpaRepository.findByPostType(postType).stream()
                .map(LostPostEntity::toDomain)
                .toList();
    }
}
