package wooteco.subway.station.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.exception.StationDuplicateException;

@JdbcTest
@Sql("classpath:tableInit.sql")
class StationDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    private StationDao stationDao;

    @Autowired
    public StationDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @DisplayName("지하철 역 저장 - 성공")
    @Test
    void insert_success() {
        // given
        final Station 코지역 = new Station("코지역");

        // when
        final Station 저장된_코지역 = stationDao.insert(코지역);

        // then
        assertThat(저장된_코지역).extracting("id").isEqualTo(1L);
        assertThat(저장된_코지역).extracting("name").isEqualTo("코지역");
    }

    @DisplayName("지하철 역 저장 - 실패, 중복된 역 존재")
    @Test
    void insert_fail_duplicateName() {
        // given
        final Station 코지역 = new Station("코지역");
        stationDao.insert(코지역);

        // when // then
        assertThatThrownBy(() -> stationDao.insert(코지역))
                .isInstanceOf(StationDuplicateException.class)
                .hasMessageContaining("코지역");
    }

    @DisplayName("전체 지하철 역 조회 - 성공")
    @Test
    void findAll_success() {
        // given
        Station 첫번째역 = new Station("첫번째역");
        Station 두번째역 = new Station("두번째역");

        // when
        stationDao.insert(첫번째역);
        stationDao.insert(두번째역);

        // then
        List<Station> stations = stationDao.findAll();
        assertThat(stations).hasSize(2);
    }

    @DisplayName("id로 지하철 역 조회 - 성공")
    @Test
    void findById_success() {
        // given
        Station 찾아볼역 = new Station("찾아볼역");
        Station 저장된_찾아볼역 = stationDao.insert(찾아볼역);
        Long 찾아볼역Id = 저장된_찾아볼역.getId();

        // when
        Optional<Station> 찾은역 = stationDao.findById(찾아볼역Id);

        // then
        assertThat(찾은역.isPresent()).isTrue();
        assertThat(찾은역.get()).isEqualTo(저장된_찾아볼역);
    }

    @DisplayName("id로 지하철 역 조회 - 실패, id 존재하지 않음")
    @Test
    void findById_fail_noId() {
        // given
        Long 찾아볼역Id = 1L;

        // when
        Optional<Station> 찾은역 = stationDao.findById(찾아볼역Id);

        // then
        assertThat(찾은역.isPresent()).isFalse();
    }

    @DisplayName("id로 지하철 역 삭제 - 성공")
    @Test
    void delete_success() {
        // given
        Station 삭제할역 = new Station("삭제할역");
        Station 저장된_삭제할역 = stationDao.insert(삭제할역);
        Long 삭제할역Id = 저장된_삭제할역.getId();

        // when // then
        assertThatCode(() -> stationDao.deleteById(삭제할역Id)).doesNotThrowAnyException();
    }

//    @DisplayName("id로 지하철 역 삭제 - 실패, 역 존재하지 않음")
//    @Test
//    void delete_success() {
//        // given
//        Station 삭제할역 = new Station("삭제할역");
//        Station 저장된_삭제할역 = stationDao.insert(삭제할역);
//        Long 삭제할역Id = 저장된_삭제할역.getId();
//
//        // when // then
//        assertThatCode(() -> stationDao.deleteById(삭제할역Id)).doesNotThrowAnyException();
//    }
}