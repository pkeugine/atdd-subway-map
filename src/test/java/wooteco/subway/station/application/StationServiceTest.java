package wooteco.subway.station.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.dao.DuplicateKeyException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.StationDuplicateException;

@MockitoSettings
class StationServiceTest {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationDao stationDao;

    @DisplayName("지하철 역 생성 - 성공")
    @Test
    void createStation_success() {
        // given
        StationRequest 피케이역_요청 = new StationRequest("피케이역");

        Station 피케이역 = new Station(1L, "피케이역");
        given(stationDao.insert(any(Station.class))).willReturn(피케이역);

        // when
        StationResponse 피케이역_응답 = stationService.createStation(피케이역_요청);

        // then
        assertThat(피케이역_응답).extracting("id").isEqualTo(피케이역.getId());
        assertThat(피케이역_응답).extracting("name").isEqualTo(피케이역.getName());
    }

    @DisplayName("지하철 역 생성 - 실패, 중복되는 이름 존재")
    @Test
    void createStation_fail_duplicatedName() {
        // given
        StationRequest 피케이역_요청 = new StationRequest("피케이역");

        given(stationDao.insert(any(Station.class))).willThrow(DuplicateKeyException.class);

        //when // then
        assertThatThrownBy(() -> stationService.createStation(피케이역_요청))
                .isInstanceOf(StationDuplicateException.class);
    }

    @DisplayName("지하철 역 전체 조회 - 성공")
    @Test
    void showStations() {
        //given
        List<Station> stations = Arrays.asList(
                new Station(1L, "정자역"),
                new Station(2L, "판교역")
        );
        given(stationDao.findAll()).willReturn(stations);

        // when
        List<StationResponse> 전체_역_응답 = stationService.findAllStationResponses();

        // then
        then(stationDao).should(times(1)).findAll();
        assertThat(전체_역_응답).hasSize(2);
    }

    @DisplayName("지하철 역 id로 삭제 - 성공")
    @Test
    void deleteStationById() {
        // given // when
        stationService.deleteStationById(any(Long.class));

        // then
        then(stationDao).should(times(1)).deleteById(any(Long.class));
    }
}