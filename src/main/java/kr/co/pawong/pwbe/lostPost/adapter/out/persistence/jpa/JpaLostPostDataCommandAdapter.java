package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.LOSTPOST_NOT_FOUND;

import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.repository.LostPostJpaRepository;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataCommandPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostStatus;
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

    @Override
    public LostPost updateLostPostOrThrow(Long postId, LostPost lostPost, Long userId) {
        return lostPostJpaRepository.findByLostPostIdAndStatus(postId, PostStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(LOSTPOST_NOT_FOUND))
                .updateBy(lostPost, userId)
                .toDomain();
    }

    @Override
    public void modifyDeleteStatusOrThrow(Long postId, Long userId) {
        lostPostJpaRepository.findByLostPostIdAndStatus(postId, PostStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(LOSTPOST_NOT_FOUND))
                .deleteBy(userId);
    }
}
