package wooteco.subway.line.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dao.StationDao;

@JdbcTest
@Sql("classpath:tableInit.sql")
@TestConstructor(autowireMode = AutowireMode.ALL)
class SectionDaoTest {

    private SectionDao sectionDao;
    private StationDao stationDao;
    private LineDao lineDao;

    public SectionDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @DisplayName("구간 생성 - 성공")
    @Test
    void insert_success() {
        // given
        Section 저장할_구간 = new Section(1L, 1L, 2L, 10);

        // when
        Section 저장된_구간 = sectionDao.insert(저장할_구간);

        // then
        assertThat(저장된_구간).extracting("id").isEqualTo(1L);
        assertThat(저장된_구간).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(저장할_구간);
    }
}