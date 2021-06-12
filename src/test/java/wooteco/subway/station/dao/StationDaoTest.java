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
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.station.domain.Station;

@JdbcTest
@Sql("classpath:tableInit.sql")
@TestConstructor(autowireMode = AutowireMode.ALL)
class StationDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    private StationDao stationDao;

    public StationDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @BeforeEach
    void setUp() {
        this.stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @DisplayName("지하철 역 저장 - 성공")
    @Test
    void insert_success() {
        // given
        Station 코지역 = new Station("코지역");

        // when
        Station 저장된_코지역 = stationDao.insert(코지역);

        // then
        assertThat(저장된_코지역).extracting("id").isEqualTo(1L);
        assertThat(저장된_코지역).extracting("name").isEqualTo("코지역");
    }

    @DisplayName("지하철 역 저장 - 실패, 중복된 역 존재")
    @Test
    void insert_fail_duplicateName() {
        // given
        Station 코지역 = new Station("코지역");
        stationDao.insert(코지역);

        // when // then
        assertThatThrownBy(() -> stationDao.insert(코지역))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("전체 지하철 역 조회 - 성공")
    @Test
    void findAll_success() {
        // given
        Station 첫번째역 = new Station("첫번째역");
        Station 두번째역 = new Station("두번째역");
        Station 저장된_첫번째역 = stationDao.insert(첫번째역);
        Station 저장된_두번째역 = stationDao.insert(두번째역);

        // when
        List<Station> stations = stationDao.findAll();

        // then
        assertThat(stations).containsExactly(저장된_첫번째역, 저장된_두번째역);
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
        assertThat(찾은역).contains(저장된_찾아볼역);
    }

    @DisplayName("id로 지하철 역 조회 - 실패, id 존재하지 않음")
    @Test
    void findById_fail_noId() {
        // given
        Long 찾아볼역Id = 1L;

        // when
        Optional<Station> 찾은역 = stationDao.findById(찾아볼역Id);

        // then
        assertThat(찾은역).isNotPresent();
    }

    @DisplayName("id로 지하철 역 삭제 - 성공")
    @Test
    void deleteById_success() {
        // given
        Station 삭제할역 = new Station("삭제할역");
        Station 저장된_삭제할역 = stationDao.insert(삭제할역);
        Long 삭제할역Id = 저장된_삭제할역.getId();

        // when // then
        assertThatCode(() -> stationDao.deleteById(삭제할역Id)).doesNotThrowAnyException();
        assertThat(stationDao.findAll()).isEmpty();
    }
}