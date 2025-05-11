package kr.co.pawong.pwbe.adoption.application.service.support;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionSearchCondition.Region;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AdoptionSearchMapperTest {

  @Test
  void 도시만_문자열로_입력된_경우_Region_객체로_파싱() {
    // given
    String onlyCity = "서울특별시";
    // when
    Optional<Region> result = AdoptionSearchMapper.parseRegion(onlyCity);
    // then
    assertThat(result).isPresent();
    assertThat(result.get().getCity()).isEqualTo("서울특별시");
    assertThat(result.get().getDistrict()).isEqualTo(null);
  }

  @Test
  void 도시와_군구가_문자열로_입력된_경우_Region_객체로_파싱() {
    // given
    String cityAndDistrict = "서울특별시 용산구";
    // when
    Optional<Region> result = AdoptionSearchMapper.parseRegion(cityAndDistrict);
    // then
    assertThat(result).isPresent();
    assertThat(result.get().getCity()).isEqualTo("서울특별시");
    assertThat(result.get().getDistrict()).isEqualTo("용산구");
  }

  @Test
  void 빈_문자열_입력시_Optional이_비어있어야_한다() {
    // given
    String blank = "";
    // when
    Optional<Region> result = AdoptionSearchMapper.parseRegion(blank);
    // then
    assertThat(result).isEmpty();
  }

  @Test
  void 공백_문자열_입력시_Optional이_비어있어야_한다() {
    // given
    String blank = " ";
    // when
    Optional<Region> result = AdoptionSearchMapper.parseRegion(blank);
    // then
    assertThat(result).isEmpty();
  }
  @Test
  void 다양한_지역문자열_리스트_입력() {
    // given
    List<String> regions = Arrays.asList("경기도 광주시","서울특별시", "인천광역시 남동구");
    // when
    List<Region> regionList = AdoptionSearchMapper.toRegionList(regions);
    // then
    assertThat(regionList).extracting(Region::getCity, Region::getDistrict)
        .contains(
            tuple("경기도", "광주시"),
            tuple("서울특별시", null),
            tuple("인천광역시", "남동구")
        );
  }

  @Test
  void null_입력시_Optional이_비어있어야_한다() {
    // given
    String blank = null;
    // when
    Optional<Region> result = AdoptionSearchMapper.parseRegion(blank);
    // then
    assertThat(result).isEmpty();
  }

  @Test
  void null_공백_빈_문자열_입력시_빈_리스트를_반환한다() {
    // given
    List<String> regions = Arrays.asList(null, "", " ");
    // when
    List<Region> regionList = AdoptionSearchMapper.toRegionList(regions);
    // then
    assertThat(regionList).isEmpty();
  }

  @Test
  void 지역_리스트가_null() {
    // given
    List<String> regions = null;
    // when
    List<Region> regionList = AdoptionSearchMapper.toRegionList(regions);
    // then
    assertThat(regionList).isEmpty();
  }


}