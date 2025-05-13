package kr.co.pawong.pwbe.lostPost.adapter.out.persistence;

import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.entity.LostPostEntity;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.repository.LostPostJpaRepository;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataCommandPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLostPostDataCommandAdapter implements LostPostDataCommandPort {

    private final LostPostJpaRepository lostPostJpaRepository;

    @Override
    public LostPost saveLostPost(LostPost lostPost) {
        return lostPostJpaRepository
                .save(LostPostEntity.from(lostPost))
                .toDomain();
    }
}
