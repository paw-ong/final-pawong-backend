package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.LOST_NOT_FOUND;

import java.util.List;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.repository.LostPostJpaRepository;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLostPostDataQueryAdapter implements LostPostDataQueryPort {

    private final LostPostJpaRepository lostPostJpaRepository;

    @Override
    public LostPost findLostPostByIdOrThrow(Long lostPostId) {
        LostPostEntity entity = lostPostJpaRepository.findById(lostPostId)
                .orElseThrow(() ->
                        new BaseException(LOST_NOT_FOUND));

        return entity.toDomain();
    }

    @Override
    public List<LostPost> getLostPostsByUserId(Long userId) {
        return lostPostJpaRepository.findByUserId(userId).stream()
                .map(LostPostEntity::toDomain)
                .toList();
    }
}
