package kr.co.pawong.pwbe.shelter.application.service;

import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterCreate;
import kr.co.pawong.pwbe.shelter.application.port.out.ShelterDataCommandPort;
import kr.co.pawong.pwbe.shelter.enums.DivisionNm;
import kr.co.pawong.pwbe.shelter.application.service.dto.ShelterApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiShelterService {
    private final ShelterDataCommandPort shelterDataCommandPort;
    private final RestTemplate restTemplate;
    @Value("${api.service-key}")
    private String serviceKey;

    public List<ShelterCreate> saveShelters() {
        List<ShelterCreate> allShelterCreates = new ArrayList<>();
        int pageNo = 1;
        int numOfRows = 1000;
        boolean hasMoreData = true;

        // 1. 미리 DB에 존재하는 careRegNo 목록을 Set으로 가져오기 (빠른 조회용)
        Set<String> existingCareRegNos = new HashSet<>(shelterDataCommandPort.findAllCareRegNos());
        log.info("이미 존재하는 보호소 수: {}", existingCareRegNos.size());

        while (hasMoreData) {
            log.info("데이터 가져오기: 페이지 {}, 페이지당 {} 건", pageNo, numOfRows);


            URI uri = UriComponentsBuilder.fromHttpUrl("https://apis.data.go.kr/1543061/animalShelterSrvc_v2/shelterInfo_v2")
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
                ResponseEntity<ShelterApi> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, ShelterApi.class);
                ShelterApi shelterApi = responseEntity.getBody();
                log.info("응답: {}", shelterApi);

                if (isValidShelterData(shelterApi)) {
                    List<ShelterApi.Item> items = shelterApi.getResponse().getBody().getItems().getItem();

                    for (ShelterApi.Item item : items) {
                        // 2. 이미 존재하는 careRegNo는 스킵
                        if (existingCareRegNos.contains(item.getCareRegNo())) {
                            log.info("중복된 보호소 (careRegNo={}): 스킵", item.getCareRegNo());
                            continue;
                        }

                        // 날짜 변환
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate dsignationDate = parseLocalDate(item.getDsignationDate(), formatter);
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
                        log.info("마지막 페이지 도달: {} 건의 데이터 검색됨", items.size());
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

        log.info("총 {}개의 신규 보호소 데이터를 가져왔습니다.", allShelterCreates.size());
        return allShelterCreates;
    }


    private boolean isValidShelterData(ShelterApi shelterApi) {
        return Optional.ofNullable(shelterApi).map(ShelterApi::getResponse).map(ShelterApi.Response::getBody).map(ShelterApi.Body::getItems).map(ShelterApi.Items::getItem).filter(items -> !items.isEmpty()).isPresent();
    }

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

    private Integer parseInt(String value, Integer defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Double parseDouble(String value, Double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // String -> Enum
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

    private String[] parseAddress(String careAddr) {
        String[] result = new String[2]; // [0] = city, [1] = district

        if (careAddr != null && !careAddr.isEmpty()) {
            String[] parts = careAddr.split(" ");
            if (parts.length >= 2) {
                result[0] = parts[0]; // city
                result[1] = parts[1]; // district
            }
        }

        return result;
    }
}
