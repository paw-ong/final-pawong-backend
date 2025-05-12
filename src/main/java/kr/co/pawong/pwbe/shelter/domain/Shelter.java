package kr.co.pawong.pwbe.shelter.domain;


import java.time.LocalDate;

import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterCreate;
import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterUpdate;
import kr.co.pawong.pwbe.shelter.enums.DivisionNm;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Shelter {

    private Long shelterId; // 보호소 id
    private String careNm; // 동물보호센터명
    private String careRegNo; // 보호소 번호
    private String orgNm; // 관리 기관명
    private DivisionNm divisionNm; // 동물보호센터 유형 - (ENUM)
    private String saveTrgtAnimal; // 구조대상동물
    private String careAddr; // 소재지 도로명 주소
    private String jibunAddr; // 소재지번주소
    private String city; // 시도
    private String district; // 시군구
    private Double lat; // 위도(double)
    private Double lng; // 경도(double)
    private LocalDate dsignationDate; // 동물보호센터 지정일자
    private String weekOprStime; // 평일 운영시작 시간
    private String weekOprEtime; // 평일 운영종료 시간
    private String weekCellStime; // 평일 분양시작 시간
    private String weekCellEtime; // 평일 분양종료 시간
    private String weekendOprStime; // 주말 운영시작 시간
    private String weekendOprEtime; // 주말 운영종료 시간
    private String weekendCellStime; // 주말 분양시작 시간
    private String weekendCellEtime; // 주말 분양종료 시간
    private String closeDay; // 휴무일
    private Integer vetPersonCnt; // 수의사 인원수
    private Integer specsPersonCnt; // 사양관리사 인원수
    private Integer medicalCnt; // 진료실 수
    private Integer breedCnt; // 사육실 수
    private Integer quarabtineCnt; // 격리실 수
    private Integer feedCnt; // 사료보관실 수
    private Integer transCarCnt; // 구조운반용차량보유대 수
    private String careTel; // 전화번호
    private LocalDate dataStdDt; // 데이터 기준일자

    // ShelterCreate -> Shelter
    public static Shelter from(ShelterCreate shelterCreate){
        return Shelter.builder()
                .shelterId(shelterCreate.getShelterId())
                .careNm(shelterCreate.getCareNm())
                .careRegNo(shelterCreate.getCareRegNo())
                .orgNm(shelterCreate.getOrgNm())
                .divisionNm(shelterCreate.getDivisionNm())
                .saveTrgtAnimal(shelterCreate.getSaveTrgtAnimal())
                .careAddr(shelterCreate.getCareAddr())
                .jibunAddr(shelterCreate.getJibunAddr())
                .city(shelterCreate.getCity())
                .district(shelterCreate.getDistrict())
                .lat(shelterCreate.getLat())
                .lng(shelterCreate.getLng())
                .dsignationDate(shelterCreate.getDsignationDate())
                .weekOprStime(shelterCreate.getWeekOprStime())
                .weekOprEtime(shelterCreate.getWeekOprEtime())
                .weekCellStime(shelterCreate.getWeekCellStime())
                .weekCellEtime(shelterCreate.getWeekCellEtime())
                .weekendOprStime(shelterCreate.getWeekendOprStime())
                .weekendOprEtime(shelterCreate.getWeekendOprEtime())
                .weekendCellStime(shelterCreate.getWeekCellStime())
                .weekendCellEtime(shelterCreate.getWeekendCellEtime())
                .closeDay(shelterCreate.getCloseDay())
                .vetPersonCnt(shelterCreate.getVetPersonCnt())
                .specsPersonCnt(shelterCreate.getSpecsPersonCnt())
                .medicalCnt(shelterCreate.getMedicalCnt())
                .breedCnt(shelterCreate.getBreedCnt())
                .quarabtineCnt(shelterCreate.getQuarabtineCnt())
                .feedCnt(shelterCreate.getFeedCnt())
                .transCarCnt(shelterCreate.getTransCarCnt())
                .careTel(shelterCreate.getCareTel())
                .dataStdDt(shelterCreate.getDataStdDt())
                .build();
    }


    // // ShelterUpdate -> Shelter
    public Shelter update(ShelterUpdate shelterUpdate){
        return Shelter.builder()
                .shelterId(shelterUpdate.getShelterId())
                .careNm(shelterUpdate.getCareNm())
                .careRegNo(shelterUpdate.getCareRegNo())
                .orgNm(shelterUpdate.getOrgNm())
                .divisionNm(shelterUpdate.getDivisionNm())
                .saveTrgtAnimal(shelterUpdate.getSaveTrgtAnimal())
                .careAddr(shelterUpdate.getCareAddr())
                .jibunAddr(shelterUpdate.getJibunAddr())
                .city(shelterUpdate.getCity())
                .district(shelterUpdate.getDistrict())
                .lat(shelterUpdate.getLat())
                .lng(shelterUpdate.getLng())
                .dsignationDate(shelterUpdate.getDsignationDate())
                .weekOprStime(shelterUpdate.getWeekOprStime())
                .weekOprEtime(shelterUpdate.getWeekOprEtime())
                .weekCellStime(shelterUpdate.getWeekCellStime())
                .weekCellEtime(shelterUpdate.getWeekCellEtime())
                .weekendOprStime(shelterUpdate.getWeekendOprStime())
                .weekendOprEtime(shelterUpdate.getWeekendOprEtime())
                .weekendCellStime(shelterUpdate.getWeekCellStime())
                .weekendCellEtime(shelterUpdate.getWeekendCellEtime())
                .closeDay(shelterUpdate.getCloseDay())
                .vetPersonCnt(shelterUpdate.getVetPersonCnt())
                .specsPersonCnt(shelterUpdate.getSpecsPersonCnt())
                .medicalCnt(shelterUpdate.getMedicalCnt())
                .breedCnt(shelterUpdate.getBreedCnt())
                .quarabtineCnt(shelterUpdate.getQuarabtineCnt())
                .feedCnt(shelterUpdate.getFeedCnt())
                .transCarCnt(shelterUpdate.getTransCarCnt())
                .careTel(shelterUpdate.getCareTel())
                .dataStdDt(shelterUpdate.getDataStdDt())
                .build();
    }


}



