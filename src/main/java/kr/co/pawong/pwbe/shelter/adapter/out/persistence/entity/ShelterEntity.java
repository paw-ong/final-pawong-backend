package kr.co.pawong.pwbe.shelter.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

import kr.co.pawong.pwbe.shelter.domain.Shelter;
import kr.co.pawong.pwbe.shelter.enums.DivisionNm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Shelter")
public class ShelterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long shelterId; // 보호소 id
    private String careNm; // 동물보호센터명
    @Column(unique = true)
    private String careRegNo; // 보호소 번호
    private String orgNm; // 관리 기관명
    @Enumerated(EnumType.STRING)
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


    // Shelter -> ShelterEntity
    public static ShelterEntity from(Shelter shelter) {
        ShelterEntity entity = new ShelterEntity();


        entity.shelterId = shelter.getShelterId();
        entity.careNm = shelter.getCareNm();
        entity.careRegNo = shelter.getCareRegNo();
        entity.orgNm = shelter.getOrgNm();
        entity.divisionNm = shelter.getDivisionNm();
        entity.saveTrgtAnimal = shelter.getSaveTrgtAnimal();
        entity.careAddr = shelter.getCareAddr();
        entity.jibunAddr = shelter.getJibunAddr();
        entity.city = shelter.getCity();
        entity.district = shelter.getDistrict();
        entity.lat = shelter.getLat();
        entity.lng = shelter.getLng();
        entity.dsignationDate = shelter.getDsignationDate();
        entity.weekOprStime = shelter.getWeekOprStime();
        entity.weekOprEtime = shelter.getWeekOprEtime();
        entity.weekCellStime = shelter.getWeekCellStime();
        entity.weekCellEtime = shelter.getWeekCellEtime();
        entity.weekendOprStime = shelter.getWeekendOprStime();
        entity.weekendOprEtime = shelter.getWeekendOprEtime();
        entity.weekendCellStime = shelter.getWeekendCellStime();
        entity.weekendCellEtime = shelter.getWeekendCellEtime();
        entity.closeDay = shelter.getCloseDay();
        entity.vetPersonCnt = shelter.getVetPersonCnt();
        entity.specsPersonCnt = shelter.getSpecsPersonCnt();
        entity.medicalCnt = shelter.getMedicalCnt();
        entity.breedCnt = shelter.getBreedCnt();
        entity.quarabtineCnt = shelter.getQuarabtineCnt();
        entity.feedCnt = shelter.getFeedCnt();
        entity.transCarCnt = shelter.getTransCarCnt();
        entity.careTel = shelter.getCareTel();
        entity.dataStdDt = shelter.getDataStdDt();

        return entity;
    }

    public Shelter toModel() {
        return Shelter.builder()
                .shelterId(this.shelterId)
                .careNm(this.careNm)
                .careRegNo(this.careRegNo)
                .orgNm(this.orgNm)
                .divisionNm(this.divisionNm)
                .saveTrgtAnimal(this.saveTrgtAnimal)
                .careAddr(this.careAddr)
                .jibunAddr(this.jibunAddr)
                .city(this.city)
                .district(this.district)
                .lat(this.lat)
                .lng(this.lng)
                .dsignationDate(this.dsignationDate)
                .weekOprStime(this.weekOprStime)
                .weekOprEtime(this.weekOprEtime)
                .weekCellStime(this.weekCellStime)
                .weekCellEtime(this.weekCellEtime)
                .weekendOprStime(this.weekendOprStime)
                .weekendOprEtime(this.weekendOprEtime)
                .weekendCellStime(this.weekendCellStime)
                .weekendCellEtime(this.weekendCellEtime)
                .closeDay(this.closeDay)
                .vetPersonCnt(this.vetPersonCnt)
                .specsPersonCnt(this.specsPersonCnt)
                .medicalCnt(this.medicalCnt)
                .breedCnt(this.breedCnt)
                .quarabtineCnt(this.quarabtineCnt)
                .feedCnt(this.feedCnt)
                .transCarCnt(this.transCarCnt)
                .careTel(this.careTel)
                .dataStdDt(this.dataStdDt)
                .build();
    }
}
