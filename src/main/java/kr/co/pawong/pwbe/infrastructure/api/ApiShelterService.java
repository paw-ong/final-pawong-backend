package kr.co.pawong.pwbe.infrastructure.api;

import static kr.co.pawong.pwbe.global.util.ApiDataUtils.convertToEnum;
import static kr.co.pawong.pwbe.global.util.ApiDataUtils.parseAddress;
import static kr.co.pawong.pwbe.global.util.ApiDataUtils.parseDouble;
import static kr.co.pawong.pwbe.global.util.ApiDataUtils.parseInt;
import static kr.co.pawong.pwbe.global.util.TimeUtils.parseLocalDate;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import kr.co.pawong.pwbe.infrastructure.api.dto.ShelterCreate;
import kr.co.pawong.pwbe.shelter.application.port.out.ShelterDataCommandPort;
import kr.co.pawong.pwbe.shelter.application.service.dto.ShelterApi;
import kr.co.pawong.pwbe.shelter.enums.DivisionNm;
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
public class ApiShelterService {

    private final ShelterDataCommandPort shelterDataCommandPort;
    private final RestTemplate restTemplate;

    @Value("${api.service-key}")
    private String serviceKey;

    @Value("${api-url.shelter}")
    private String apiUrl;

    public List<ShelterCreate> saveShelters() {
        List<ShelterCreate> allShelterCreates = new ArrayList<>();
        int pageNo = 1;
        int numOfRows = 1000;
        boolean hasMoreData = true;

        // 1. 미리 DB에 존재하는 careRegNo 목록을 Set으로 가져오기 (빠른 조회용)
        Set<String> existingCareRegNos = new HashSet<>(shelterDataCommandPort.findAllCareRegNos());
        log.info("이미 존재하는 보호소 수: {}", existingCareRegNos.size());

        while (hasMoreData) {

            URI uri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("numOfRows", numOfRows)
                    .queryParam("pageNo", pageNo)
                    .queryParam("_type", "json")
                    .queryParam("MobileOS", "ETC")
                    .queryParam("MobileApp", "AppTest")
                    .build(true)
                    .toUri();

            log.info("요청 주소: {}", uri);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);

            try {
                ResponseEntity<ShelterApi> responseEntity = restTemplate.exchange(uri,
                        HttpMethod.GET, entity, ShelterApi.class);
                ShelterApi shelterApi = responseEntity.getBody();
                log.info("응답: {}", shelterApi);

                if (isValidShelterData(shelterApi)) {
                    List<ShelterApi.Item> items = shelterApi.getResponse().getBody().getItems()
                            .getItem();

                    for (ShelterApi.Item item : items) {
                        // 2. 이미 존재하는 careRegNo는 스킵
                        if (existingCareRegNos.contains(item.getCareRegNo())) {
                            log.info("중복된 보호소 (careRegNo={}): 스킵", item.getCareRegNo());
                            continue;
                        }

                        // 날짜 변환
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate dsignationDate = parseLocalDate(item.getDsignationDate(),
                                formatter);
                        LocalDate dataStdDt = parseLocalDate(item.getDataStdDt(), formatter);
                        String[] addr = parseAddress(item.getCareAddr());

                        ShelterCreate shelterCreate = ShelterCreate.builder()
                                .careNm(item.getCareNm())
                                .careRegNo(item.getCareRegNo())
                                .orgNm(item.getOrgNm())
                                .divisionNm(convertToEnum(item.getDivisionNm(), DivisionNm.class))
                                .saveTrgtAnimal(item.getSaveTrgtAnimal())
                                .careAddr(item.getCareAddr())
                                .jibunAddr(item.getJibunAddr())
                                .city(addr[0])
                                .district(addr[1])
                                .lat(parseDouble(item.getLat(), null))
                                .lng(parseDouble(item.getLng(), null))
                                .dsignationDate(dsignationDate)
                                .weekOprStime(item.getWeekOprStime())
                                .weekOprEtime(item.getWeekOprEtime())
                                .weekCellStime(item.getWeekCellStime())
                                .weekCellEtime(item.getWeekCellEtime())
                                .weekendOprStime(item.getWeekendOprStime())
                                .weekendOprEtime(item.getWeekendOprEtime())
                                .weekendCellStime(item.getWeekendCellStime())
                                .weekendCellEtime(item.getWeekendCellEtime())
                                .closeDay(item.getCloseDay())
                                .vetPersonCnt(parseInt(item.getVetPersonCnt(), null))
                                .specsPersonCnt(parseInt(item.getSpecsPersonCnt(), null))
                                .medicalCnt(parseInt(item.getMedicalCnt(), null))
                                .breedCnt(parseInt(item.getBreedCnt(), null))
                                .quarabtineCnt(parseInt(item.getQuarabtineCnt(), null))
                                .feedCnt(parseInt(item.getFeedCnt(), null))
                                .transCarCnt(parseInt(item.getTransCarCnt(), null))
                                .careTel(item.getCareTel())
                                .dataStdDt(dataStdDt)
                                .build();

                        allShelterCreates.add(shelterCreate);
                    }

                    if (items.size() < numOfRows) {
                        hasMoreData = false;
                        log.info("{} 건의 데이터 검색됨", items.size());
                    } else {
                        pageNo++;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } else {
                    hasMoreData = false;
                    log.info("더 이상 데이터가 없습니다.");
                }
            } catch (Exception e) {
                log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
                hasMoreData = false;
            }
        }
        return allShelterCreates;
    }

    // API 응답 데이터의 유효성을 검사
    private boolean isValidShelterData(ShelterApi shelterApi) {
        return Optional.ofNullable(shelterApi)
                .map(ShelterApi::getResponse)
                .map(ShelterApi.Response::getBody)
                .map(ShelterApi.Body::getItems).map(ShelterApi.Items::getItem)
                .filter(items -> !items.isEmpty())
                .isPresent();
    }
}
