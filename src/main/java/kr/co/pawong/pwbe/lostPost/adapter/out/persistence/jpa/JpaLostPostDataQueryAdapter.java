package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa;

import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLostPostDataQueryAdapter implements LostPostDataQueryPort {

    private final LostPostJpaRepository lostPostJpaRepository;

    @Override
    public LostPost findLostPostById(Long lostPostId) {
        return lostPostJpaRepository.findById(lostPostId)
                .get().toDomain();
    }

}
