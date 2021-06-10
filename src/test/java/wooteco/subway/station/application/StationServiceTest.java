package wooteco.subway.station.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import wooteco.subway.station.dao.StationDao;

@MockitoSettings
class StationServiceTest {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationDao stationDao;

    @DisplayName("지하철 역 생성 - 성공")
    @Test
    void createStation() {
        // given

        // when

        // then
    }

    @DisplayName("지하철 역 전체 조회 - 성공")
    @Test
    void showStations() {
        // given

        // when

        // then
    }

    @DisplayName("지하철 역 id로 삭제 - 성공")
    @Test
    void deleteStationById() {
        // given

        // when

        // then
    }
}