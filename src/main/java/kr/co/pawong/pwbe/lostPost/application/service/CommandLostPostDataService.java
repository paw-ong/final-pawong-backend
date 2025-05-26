package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.CommandLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalMessagePublishPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataCommandPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.CreatedLostAnimalPublishDto;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommandLostPostDataService implements CommandLostPostDataUseCase {

    private final LostPostDataCommandPort lostPostUpdatePort;

    private final LostAnimalMessagePublishPort lostAnimalMessagePublishPort;

    @Override
    public Long createLostPost(LostPost lostPost, Long userId) {
        lostPost.createBy(userId);
        return lostPostUpdatePort.saveLostPost(lostPost).getLostPostId();
    }

    @Override
    @Transactional
    public Long updateLostPost(Long postId, LostPost lostPost, Long userId) {
        // RDB_에 저장
        LostPost newPost = lostPostUpdatePort.updateLostPostOrThrow(postId, lostPost, userId);
        // 실종/발견 동물 추가 메시지 발행
        lostAnimalMessagePublishPort.publishLostAnimalCreatedMessage(
                new CreatedLostAnimalPublishDto(
                        newPost.getLostPostId(),
                        newPost.getPostType(),
                        String.join(" ", newPost.getColor(), newPost.getKindNm(), newPost.getUpKindNm().name()),
                        // TODO: 이거 이렇게 쓰는거 맞나요...?
                        newPost.getImageKey()
                )
        );
        return newPost.getLostPostId();
    }

    @Override
    @Transactional
    public void deleteLostPost(Long postId, Long userId) {
        lostPostUpdatePort.modifyDeleteStatusOrThrow(postId, userId);
    }
}
