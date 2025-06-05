package kr.co.pawong.pwbe.lostPost.application.service;

import java.net.URL;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import kr.co.pawong.pwbe.adoption.application.port.out.ProxyUrlPort;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.infrastructure.s3.application.port.out.ImageStoragePort;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostAnimalDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalQuery;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostCardMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAdoptionDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalEngineCommandPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.ShelterCareNmPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.UserInfoPort;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryLostAnimalDataService implements QueryLostAnimalDataUseCase {

    private final LostAnimalEngineCommandPort lostAnimalEngineCommandPort;
    private final LostAdoptionDataQueryPort lostAdoptionDataQueryPort;
    private final LostPostDataQueryPort lostPostDataQueryPort;
    private final ShelterCareNmPort shelterCareNmPort;
    private final UserInfoPort userInfoPort;
    private final ImageStoragePort imageStoragePort;
    private final Clock clock;
    private static final Duration DOWNLOAD_URL_EXPIRE = Duration.ofMinutes(15);
    private final ProxyUrlPort proxyUrlPort;

    @Override
    @Transactional(readOnly = true)
    public List<LostPostCard> getLostAnimalsByIds(List<LostAnimalQuery> lostAnimalQueries) {

        return lostAnimalQueries.stream()
                .map(this::convertToLostPostCardOrNull)
                .filter(Objects::nonNull)
                .toList();
    }

    // TODO: 리팩터링 필요...
    private LostPostCard convertToLostPostCardOrNull(LostAnimalQuery lostAnimalQuery) {
        return switch (lostAnimalQuery.type()) {
            // Lost Post 가져오기
            case LOST_POST -> {
                LostPost lostPost = getLostPostByIdOrDeleteFromEngine(lostAnimalQuery);
                if (lostPost == null) {
                    yield null;
                }
                // 작성자 이름 조회
                String author = userInfoPort.getNicknameByUserId(lostPost.getUserId());
                URL url = imageStoragePort.presignDownload(lostPost.getImageKey(), DOWNLOAD_URL_EXPIRE);
                yield LostPostCardMapper.toLostPostCard(lostPost, author, clock, url);
            }
            // Lost Adoption 가져오기
            case LOST_ADOPTION -> {
                LostAdoption lostAdoption = getLostAdoptionByIdOrDeleteFromEngine(lostAnimalQuery);
                if (lostAdoption == null) {
                    yield null;
                }
                changePopfilesToProxy(lostAdoption);
                // 보호소 이름 조회
                String shelter = shelterCareNmPort.getShelterCareNmByCareRegNo(
                        lostAdoption.getCareRegNo());
                yield LostPostCardMapper.toLostPostCard(lostAdoption, shelter, clock);
            }
        };
    }

    private LostPost getLostPostByIdOrDeleteFromEngine(LostAnimalQuery lostAnimalQuery) {
        try {
            return lostPostDataQueryPort.findLostPostByIdOrThrow(lostAnimalQuery.id());
        } catch (BaseException e) {
            // ES_에서 해당 데이터 삭제 -> ES와 RDB 싱크 맞추기
            // TODO: 현재 함수 구성으로는 FOUND인지 LOST인지 알 수 없어서 둘 다 삭제하게 했습니다...
            lostAnimalEngineCommandPort.deleteLostAnimal(
                    "FOUND_" + lostAnimalQuery.id()
            );
            lostAnimalEngineCommandPort.deleteLostAnimal(
                    "LOST_" + lostAnimalQuery.id()
            );
            log.info("RDB에 없는 ES 데이터여서 ES에서 삭제합니다. type: {}, id: {}",
                    lostAnimalQuery.type().name(), lostAnimalQuery.id());
            return null;
        }
    }

    private LostAdoption getLostAdoptionByIdOrDeleteFromEngine(LostAnimalQuery lostAnimalQuery) {
        try {
            return lostAdoptionDataQueryPort.findAdoptionByIdOrThrow(
                    lostAnimalQuery.id());
        } catch (BaseException e) {
            // ES_에서 해당 데이터 삭제 -> ES와 RDB 싱크 맞추기
            // TODO: 현재 하드코딩 됨. 추후 이 클래스의 조회 로직 자체를 아예 리팩토링 해야 할 것 같습니다..
            lostAnimalEngineCommandPort.deleteLostAnimal(
                    "FOSTER_" + lostAnimalQuery.id()
            );
            log.info("RDB에 없는 ES 데이터여서 ES에서 삭제합니다. type: FOSTER, id: {}", lostAnimalQuery.id());
            return null;
        }
    }

    private void changePopfilesToProxy(LostAdoption lostAdoption) {
        String popfile1 = proxyUrlPort.generateProxyUrl(lostAdoption.getPopfile1());
        String popfile2 = proxyUrlPort.generateProxyUrl(lostAdoption.getPopfile2());
        lostAdoption.updatePopfile1(popfile1);
        lostAdoption.updatePopfile2(popfile2);
    }
}
