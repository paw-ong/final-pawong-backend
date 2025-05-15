package kr.co.pawong.pwbe.lostPost.application.service;

import java.time.Clock;
import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalQuery;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostCardMapper;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostDetailMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAdoptionDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.ShelterCareNmPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.UserInfoPort;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueryLostPostDataService implements QueryLostPostDataUseCase {

    private final LostPostDataQueryPort lostPostDataQueryPort;
    private final LostAdoptionDataQueryPort lostAdoptionDataQueryPort;
    private final ShelterCareNmPort shelterCareNmPort;
    private final UserInfoPort userInfoPort;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public List<LostPostCard> getLostPostsByUserId(Long userId) {

        List<LostPost> lostPosts = lostPostDataQueryPort.getLostPostsByUserId(userId);
        String author = userInfoPort.getNicknameByUserId(userId);

        return lostPosts.stream()
                .map(post -> LostPostCardMapper.toLostPostCard(post, author, clock)).toList();
    }

    @Override
    public LostPostDetailResponse findLostPostById(Long lostPostId) {

        LostPost lostPost = lostPostDataQueryPort.findLostPostByIdOrThrow(lostPostId);
        String author = userInfoPort.getNicknameByUserId(lostPost.getUserId());

        LostPostDetailDto lostPostDetailDto = LostPostDetailMapper.toModel(lostPost, author, clock);

        return new LostPostDetailResponse(lostPostDetailDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LostPostCard> getLostAnimalsByIds(List<LostAnimalQuery> lostAnimalQueries) {

        return lostAnimalQueries.stream()
                .map(this::convertToLostPostCard)
                .toList();
    }

    private LostPostCard convertToLostPostCard(LostAnimalQuery lostAnimalQuery) {
        return switch (lostAnimalQuery.type()) {
            // Lost Post 가져오기
            case LOST_POST -> {
                LostPost lostPost = lostPostDataQueryPort.findLostPostByIdOrThrow(
                        lostAnimalQuery.id());
                // 작성자 이름 조회
                String author = userInfoPort.getNicknameByUserId(lostPost.getUserId());
                yield LostPostCardMapper.toLostPostCard(lostPost, author, clock);
            }
            // Lost Adoption 가져오기
            case LOST_ADOPTION -> {
                LostAdoption lostAdoption = lostAdoptionDataQueryPort.findAdoptionById(
                        lostAnimalQuery.id());
                // 보호소 이름 조회
                String shelter = shelterCareNmPort.getShelterCareNmByCareRegNo(
                        lostAdoption.getCareRegNo());
                yield LostPostCardMapper.toLostPostCard(lostAdoption, shelter, clock);
            }
        };
    }
}
