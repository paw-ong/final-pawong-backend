package kr.co.pawong.pwbe.infrastructure.api;

import static kr.co.pawong.pwbe.global.util.ApiDataUtils.convertToEnum;
import static kr.co.pawong.pwbe.global.util.ApiDataUtils.parseIntAge;
import static kr.co.pawong.pwbe.global.util.TimeUtils.parseLocalDate;
import static kr.co.pawong.pwbe.global.util.TimeUtils.parseLocalDateTime;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.adoption.application.port.in.ApiRequestUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCareDto;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCreate;
import kr.co.pawong.pwbe.adoption.application.port.out.ShelterCommandPort;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi.Body;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi.Items;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi.Response;
import kr.co.pawong.pwbe.adoption.enums.NeuterYn;
import kr.co.pawong.pwbe.adoption.enums.ProcessState;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiAdoptionService implements ApiRequestUseCase {

    private final RestTemplate restTemplate;
    private final ShelterCommandPort shelterCommandPort;

    @Value("${api.service-key}")
    private String serviceKey;

    @Value("${api-url.adoption}")
    private String apiUrl;

    private int pageNo = 1;
    private int numOfRows = 1000;
    private List<AdoptionApi.Item> items;
    private int index = 0;
    private boolean dataExhausted = false;

    // 공공데이터 API -> 유기동물 아이템
    @Override
    public AdoptionApi.Item fetchNextAdoptionItem() {
        // 현재 아이템 목록이 없거나 모든 아이템을 처리한 경우 새로운 데이터 요청
        if (items == null || index >= items.size()) {
            // 이미 모든 데이터를 가져온 경우 null 반환
            if (dataExhausted) {
                return null;
            }

            // API에서 다음 페이지 데이터 가져오기
            AdoptionApi adoptionApi = fetchAdoptionData(pageNo, numOfRows);

            // 유효한 데이터가 아닌 경우 null 반환
            if (!isValidAdoptionData(adoptionApi)) {
                dataExhausted = true;
                return null;
            }

            // 응답에서 아이템 목록 추출
            items = adoptionApi.getResponse().getBody().getItems().getItem();
            // 다음 페이지로 이동
            pageNo++;
            // 인덱스 초기화
            index = 0;

            // 가져온 아이템 수가 요청한 행 수보다 적으면 마지막 페이지로 간주
            if (items.size() < numOfRows) {
                dataExhausted = true;
            }
        }

        // 현재 인덱스의 아이템 반환하고 인덱스 증가
        return items.get(index++);
    }

    // 공공데이터 API에서 유기동물 정보를 가져오는 메서드
    private AdoptionApi fetchAdoptionData(int pageNo, int numOfRows) {
        // API 요청 주소
        URI uri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("serviceKey",
                        serviceKey)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("_type", "json")
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "AppTest")
                .build(true)
                .toUri();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<AdoptionApi> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
                    entity, AdoptionApi.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
            return null;
        }
    }

    // 공공데이터 API item -> AdoptionCreate
    @Override
    public AdoptionCreate convertToAdoptionCreate(AdoptionApi.Item item) {
        // 날짜/시간 정의
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 날짜 및 시간 데이터 파싱
        LocalDate happenDt = parseLocalDate(item.getHappenDt(), dateFormatter);
        LocalDate noticeSdt = parseLocalDate(item.getNoticeSdt(), dateFormatter);
        LocalDate noticeEdt = parseLocalDate(item.getNoticeEdt(), dateFormatter);
        LocalDateTime updTm = parseLocalDateTime(item.getUpdTm(), dateTimeFormatter);

        // AdoptionCreate 생성 및 전처리
        AdoptionCreate adoptionCreate = AdoptionCreate.builder()
                .desertionNo(item.getDesertionNo())
                .happenDt(happenDt)
                .happenPlace(item.getHappenPlace())
                .upKindCd(UpKindCd.fromValue(item.getUpKindCd()))
                .upKindNm(convertToEnum(item.getUpKindNm(), UpKindNm.class))
                .kindCd(item.getKindCd())
                .kindNm(item.getKindNm())
                .colorCd(item.getColorCd())
                .age(parseIntAge(item.getAge()))
                .weight(item.getWeight())
                .noticeNo(item.getNoticeNo())
                .noticeSdt(noticeSdt)
                .noticeEdt(noticeEdt)
                .popfile1(item.getPopfile1())
                .popfile2(item.getPopfile2())
                .processState(ProcessState.fromValue(item.getProcessState()))
                .sexCd(convertToEnum(item.getSexCd(), SexCd.class))
                .neuterYn(convertToEnum(item.getNeuterYn(), NeuterYn.class))
                .specialMark(item.getSpecialMark())
                .careRegNo(item.getCareRegNo())
                .updTm(updTm)
                .build();

        // ActiveState 설정
        adoptionCreate.updateActiveState();

        return adoptionCreate;
    }

    // API 응답 데이터의 유효성을 검사
    private boolean isValidAdoptionData(AdoptionApi adoptionApi) {
        return Optional.ofNullable(adoptionApi)
                .map(AdoptionApi::getResponse)
                .map(Response::getBody)
                .map(Body::getItems)
                .map(Items::getItem)
                .filter(items -> !items.isEmpty())
                .isPresent();
    }

    @Override
    public void extractAndProcessShelterInfo(AdoptionApi.Item item) {
        try {
            // 보호소 정보 추출
            AdoptionCareDto adoptionCareDto = AdoptionCareDto.from(item);

            // 추출한 정보 처리
            shelterCommandPort.processShelterInfo(adoptionCareDto);
        } catch (Exception e) {
            log.error("보호소 정보 처리 중 오류 발생: careRegNo={}, error={}",
                    item.getCareRegNo(), e.getMessage(), e);
            throw e;
        }
    }
}

