package kr.co.pawong.pwbe.adoption.application.service;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.adoption.application.port.in.ApiRequestUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.UpdateAdoptionDataUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCreate;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi.Body;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi.Items;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi.Response;
import kr.co.pawong.pwbe.adoption.enums.NeuterYn;
import kr.co.pawong.pwbe.adoption.enums.ProcessState;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi.Body;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi.Items;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi.Response;
import kr.co.pawong.pwbe.adoption.application.port.in.CommandAdoptionDataUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.ApiRequestUseCase;
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
public class ApiRequestService implements ApiRequestUseCase {

    private final RestTemplate restTemplate;
    private final CommandAdoptionDataUseCase updateAdoptionDataUseCase;

    @Value("${api.service-key}")
    private String serviceKey;

    /**
     * 공공데이터 API에서 유기동물 정보를 가져와 저장하는 메서드
     */
    @Override
    public void fetchAndSaveAdoptions() {
        int pageNo = 1; // 시작 페이지 번호
        int numOfRows = 1000; // 한 페이지당 가져올 데이터 수
        boolean hasMoreData = true; // 더 가져올 데이터가 있는지 여부
        int totalSaved = 0; // 총 저장된 개수

        while (hasMoreData) {
//            log.info("데이터 가져오기: 페이지 {}, 페이지당 {} 건", pageNo, numOfRows);

            // 1. API에서 데이터 가져오기
            AdoptionApi adoptionApi = fetchAdoptionData(pageNo, numOfRows);

            // 데이터가 없거나 오류가 발생한 경우 종료
            if (!isValidAdoptionData(adoptionApi)) {
                log.info("더 이상 데이터가 없습니다.");
                break;
            }

            // 2. API 응답 데이터를 AdoptionCreate로 변환
            List<AdoptionApi.Item> items = adoptionApi.getResponse().getBody().getItems().getItem();
            List<AdoptionCreate> adoptionCreates = convertToAdoptionCreates(items);

            // 3. 변환된 데이터를 리스트에 추가
            updateAdoptionDataUseCase.saveAdoptions(adoptionCreates);

            totalSaved += adoptionCreates.size();
            log.info("페이지 {} 처리 및 저장 완료: {} 건의 데이터 저장됨 (현재까지 총 {}건)",
                    pageNo, adoptionCreates.size(), totalSaved);

            if (items.size() < numOfRows) {
                log.info("마지막 페이지 도달: 총 {} 건의 데이터 수집 완료", totalSaved);
                hasMoreData = false;
            } else {
                pageNo++;
                // API 호출 간 지연 시간 설정
                loading(100);
            }
        }
    }

    /**
     * 공공데이터 API에서 유기동물 정보를 가져오는 메서드
     *
     * @param pageNo    페이지 번호
     * @param numOfRows 한 페이지당 가져올 데이터 수
     * @return API 응답 데이터
     */
    private AdoptionApi fetchAdoptionData(int pageNo, int numOfRows) {
        // API 요청 주소
        URI uri = UriComponentsBuilder.fromHttpUrl(
                        "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/abandonmentPublic_v2")
                .queryParam("serviceKey",
                        serviceKey)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("_type", "json")
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "AppTest")
                .build(true)
                .toUri();

//        log.info("요청 주소: {}", uri.toString());

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<AdoptionApi> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
                    entity, AdoptionApi.class);
            //            log.info("응답 수신 완료");
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 공공데이터 API의 Item을 AdoptionCreate로 변환하는 메서드
     * - 날짜 파싱
     *
     * @param items 공공데이터 API에서 받아온 Item 리스트
     * @return 변환된 AdoptionCreate 리스트
     */
    private List<AdoptionCreate> convertToAdoptionCreates(List<AdoptionApi.Item> items) {
        List<AdoptionCreate> adoptionCreates = new ArrayList<>();
        // 날짜/시간 정의
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        for (AdoptionApi.Item item : items) {
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
                    .age(parseInt(item.getAge()))
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

            // 리스트에 추가
            adoptionCreates.add(adoptionCreate);
        }

        // AdoptionCreate 리스트 변환
        return adoptionCreates;
    }

    /**
     * API 응답 데이터의 유효성을 검사하는 메서드
     *
     * @param adoptionApi API 응답 데이터
     * @return 유효한 데이터가 있으면 true, 없으면 false
     */
    private boolean isValidAdoptionData(AdoptionApi adoptionApi) {
        return Optional.ofNullable(adoptionApi)
                .map(AdoptionApi::getResponse)
                .map(Response::getBody)
                .map(Body::getItems)
                .map(Items::getItem)
                .filter(items -> !items.isEmpty())
                .isPresent();
    }

    /**
     * API 호출 간 지연 시간을 설정하는 메서드
     * @param millis 지연 시간(밀리초)
     */
    private void loading(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("스레드 지연 중 인터럽트 발생");
        }
    }

    /**
     * 문자열을 LocalDate로 변환하는 메서드
     *
     * @param date      변환할 날짜 문자열
     * @param formatter 날짜 형식
     * @return 변환된 LocalDate 객체
     */
    private LocalDate parseLocalDate(String date, DateTimeFormatter formatter) {
        if (date != null && !date.isEmpty()) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                log.error("날짜 파싱 오류 ({}): {}", date, e.getMessage());
            }
        }
        return null;
    }

    /**
     * 문자열을 LocalDateTime으로 변환하는 메서드
     *
     * @param date      변환할 날짜 시간 문자열
     * @param formatter 날짜 시간 형식
     * @return 변환된 LocalDateTime 객체
     */
    private LocalDateTime parseLocalDateTime(String date, DateTimeFormatter formatter) {
        if (date != null && !date.isEmpty()) {
            try {
                return LocalDateTime.parse(date, formatter)
                        .truncatedTo(ChronoUnit.SECONDS);
            } catch (DateTimeParseException e) {
                log.error("날짜 시간 파싱 오류 ({}): {}", date, e.getMessage());
            }
        }
        return null;
    }

    /**
     * 문자열을 정수로 변환하는 메서드
     *
     * @param value 변환할 문자열
     * @return 변환된 정수 값
     */
    private Integer parseInt(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            String age = value.substring(0, 4);
            return Integer.parseInt(age);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 문자열을 Enum으로 변환하는 메서드
     *
     * @param data      변환할 문자열
     * @param enumClass Enum 클래스
     * @return 변환된 Enum 값
     */
    private <T extends Enum<T>> T convertToEnum(String data, Class<T> enumClass) {
        if (data == null) {
            return null;
        }

        try {
            return Enum.valueOf(enumClass, data);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

