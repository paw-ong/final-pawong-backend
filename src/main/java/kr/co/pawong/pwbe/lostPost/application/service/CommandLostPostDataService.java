package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.CommandLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalMessagePublishPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataCommandPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.StorageDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.CreatedLostAnimalPublishDto;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.user.application.port.in.ToggleBookmarkUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommandLostPostDataService implements CommandLostPostDataUseCase {

    private final LostPostDataCommandPort lostPostUpdatePort;
    private final LostAnimalMessagePublishPort lostAnimalMessagePublishPort;
    private final StorageDataQueryPort storageDataQueryPort;
    private final ToggleBookmarkUseCase toggleBookmarkUseCase;

    @Override
    public Long createLostPost(LostPost lostPost, Long userId) {
        lostPost.createBy(userId);
        LostPost newPost = lostPostUpdatePort.saveLostPost(lostPost);
        // 실종/발견 동물 추가 메시지 발행
        String imageUrl = storageDataQueryPort.acquireImageUrl(newPost.getImageKey());
        String textFeature = String.join(" ", newPost.getColor(), newPost.getKindNm(),
                newPost.getUpKindNm().name());
        lostAnimalMessagePublishPort.publishLostAnimalCreatedMessage(
                new CreatedLostAnimalPublishDto(
                        newPost.getLostPostId(),
                        newPost.getPostType(),
                        textFeature,
                        imageUrl
                )
        );
        return newPost.getLostPostId();
    }

    @Override
    @Transactional
    public Long updateLostPost(Long postId, LostPost lostPost, Long userId) {
        // RDB_에 저장
        LostPost newPost = lostPostUpdatePort.updateLostPostOrThrow(postId, lostPost, userId);
        // 실종/발견 동물 추가 메시지 발행
        String imageUrl = storageDataQueryPort.acquireImageUrl(newPost.getImageKey());
        String textFeature = String.join(" ", newPost.getColor(), newPost.getKindNm(),
                newPost.getUpKindNm().name());
        lostAnimalMessagePublishPort.publishLostAnimalCreatedMessage(
                new CreatedLostAnimalPublishDto(
                        newPost.getLostPostId(),
                        newPost.getPostType(),
                        textFeature,
                        imageUrl
                )
        );
        return newPost.getLostPostId();
    }

    @Override
    @Transactional
    public void deleteLostPost(Long postId, Long userId) {
        toggleBookmarkUseCase.deleteByLostPostId(postId);
        lostPostUpdatePort.modifyDeleteStatusOrThrow(postId, userId);
    }
}
