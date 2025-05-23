package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.LOST_NOT_FOUND;

import java.util.List;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.request.LostPostSearchRequest;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.query.LostPostQueryBuilder;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.repository.LostPostJpaRepository;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLostPostDataQueryAdapter implements LostPostDataQueryPort {

    private final LostPostJpaRepository lostPostJpaRepository;
    private final LostPostQueryBuilder lostPostQueryBuilder;

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

    @Override
    public Page<LostPost> getLostPostsByPostTypePaged(Pageable pageable, PostType type) {
        Page<LostPostEntity> entityPage = lostPostJpaRepository.findByPostType(type, pageable);
        return entityPage.map(LostPostEntity::toDomain);
    }

    @Override
    public Page<LostPost> searchLostPosts(Pageable pageable, LostPostSearchRequest request) {
        Page<LostPostEntity> entityPage = lostPostQueryBuilder.buildFilterQuery(pageable, request);
        return entityPage.map(LostPostEntity::toDomain);
    }
}
