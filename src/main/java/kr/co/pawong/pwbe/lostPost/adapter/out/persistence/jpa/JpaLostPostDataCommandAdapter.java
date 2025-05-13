package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa;

import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataCommandPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLostPostDataCommandAdapter implements LostPostDataCommandPort {

    private final kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.LostPostJpaRepository lostPostJpaRepository;

    @Override
    public LostPost saveLostPost(LostPost lostPost) {
        return lostPostJpaRepository
                .save(LostPostEntity.from(lostPost))
                .toDomain();
    }
}
