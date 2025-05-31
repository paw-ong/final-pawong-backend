package kr.co.pawong.pwbe.lostPost.application.service;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import kr.co.pawong.pwbe.infrastructure.s3.application.port.out.ImageStoragePort;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostAnimalDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalQuery;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostCardMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.BookmarkInfoPort;
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
public class QueryLostAnimalDataService implements QueryLostAnimalDataUseCase {

    private final LostAdoptionDataQueryPort lostAdoptionDataQueryPort;
    private final LostPostDataQueryPort lostPostDataQueryPort;
    private final ShelterCareNmPort shelterCareNmPort;
    private final UserInfoPort userInfoPort;
    private final BookmarkInfoPort bookmarkInfoPort;
    private final ImageStoragePort imageStoragePort;
    private final Clock clock;
    private static final Duration DOWNLOAD_URL_EXPIRE = Duration.ofMinutes(15);

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
                String url = imageStoragePort.presignDownload(lostPost.getImageKey(), DOWNLOAD_URL_EXPIRE).toString();
                // 유저 북마크 여부
                boolean bookmarked = bookmarkInfoPort.existsByUserIdAndLostPostId(lostAnimalQuery.userId(),
                        lostAnimalQuery.id());
                yield LostPostCardMapper.toLostPostCard(lostPost, author, bookmarked, clock, url);
            }
            // Lost Adoption 가져오기
            case LOST_ADOPTION -> {
                LostAdoption lostAdoption = lostAdoptionDataQueryPort.findAdoptionByIdOrThrow(
                        lostAnimalQuery.id());
                // 보호소 이름 조회
                String shelter = shelterCareNmPort.getShelterCareNmByCareRegNo(
                        lostAdoption.getCareRegNo());
                // 유저 북마크 여부
                boolean bookmarked = bookmarkInfoPort.existsByUserIdAndAdoptionId(lostAnimalQuery.userId(),
                        lostAnimalQuery.id());
                yield LostPostCardMapper.toLostPostCard(lostAdoption, shelter, bookmarked, clock);
            }
        };
    }
}
