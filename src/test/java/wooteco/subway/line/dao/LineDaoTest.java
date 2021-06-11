package wooteco.subway.line.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.line.domain.Line;

@JdbcTest
@Sql("classpath:tableInit.sql")
class LineDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    private LineDao lineDao;

    @Autowired
    public LineDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @DisplayName("노선 생성 - 성공")
    @Test
    void createLine_success() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600");

        // when
        Line 저장된_신분당선 = lineDao.insert(신분당선);

        // then
        assertThat(저장된_신분당선).extracting("id").isEqualTo(1L);
        assertThat(저장된_신분당선).extracting("name").isEqualTo(신분당선.getName());
        assertThat(저장된_신분당선).extracting("color").isEqualTo(신분당선.getColor());
    }

    @DisplayName("노선 생성 - 실패, 중복된 노선 이름 존재")
    @Test
    void createLine_fail_duplicateName() {
        // given
        Line 신분당선_빨강 = new Line("신분당선", "bg-red-600");
        Line 신분당선_파랑 = new Line("신분당선", "bg-blue-600");
        lineDao.insert(신분당선_빨강);

        //when // then
        assertThatThrownBy(() -> lineDao.insert(신분당선_파랑))
                .isInstanceOf(DuplicateKeyException.class);

    }

    @DisplayName("노선 생성 - 실패, 중복된 노선 색상 존재")
    @Test
    void createLine_fail_duplicateColor() {
        // given
        Line 신분당선_빨강 = new Line("신분당선", "bg-red-600");
        Line 분당선_빨강 = new Line("분당선", "bg-red-600");
        lineDao.insert(신분당선_빨강);

        //when // then
        assertThatThrownBy(() -> lineDao.insert(분당선_빨강))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("전체 노선 조회 - 성공")
    @Test
    void findAll_success() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600");
        Line 분당선 = new Line("분당선", "bg-yellow-600");
        Line 저장된_신분당선 = lineDao.insert(신분당선);
        Line 저장된_분당선 = lineDao.insert(분당선);

        // when
        List<Line> lines = lineDao.findAll();

        // then
        assertThat(lines).containsExactly(저장된_신분당선, 저장된_분당선);
    }

    @DisplayName("id로 노선 조회 - 성공")
    @Test
    void findById_success() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600");
        Line 저장된_신분당선 = lineDao.insert(신분당선);
        Long 찾아볼Id = 저장된_신분당선.getId();

        // when
        Optional<Line> 찾은노선 = lineDao.findById(찾아볼Id);

        // then
        assertThat(찾은노선).contains(저장된_신분당선);
    }

    @DisplayName("id로 노선 조회 - 실패, id 존재하지 않음")
    @Test
    void findById_fail_noId() {
        // given
        Long 찾아볼Id = 1L;

        // when
        Optional<Line> 찾은노선 = lineDao.findById(찾아볼Id);

        // then
        assertThat(찾은노선).isNotPresent();
    }

    @DisplayName("노선 수정 - 성공")
    @Test
    void update_success() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600");
        Line 저장된_신분당선 = lineDao.insert(신분당선);

        Line 이름_색상_수정될_신분당선 = new Line(저장된_신분당선.getId(), "구분당선", "bg-green-600");

        // when
        lineDao.update(이름_색상_수정될_신분당선);

        // then
        Optional<Line> 수정된_노선 = lineDao.findById(이름_색상_수정될_신분당선.getId());
        assertThat(수정된_노선).contains(이름_색상_수정될_신분당선);
    }

    @DisplayName("id로 노선 삭제 - 성공")
    @Test
    void deleteById_success() {
        // given
        Line 삭제할_노선 = new Line("신분당선", "bg-red-600");
        Line 저장된_삭제할노선 = lineDao.insert(삭제할_노선);
        Long 삭제할_노선Id = 저장된_삭제할노선.getId();

        // when // then
        assertThatCode(() -> lineDao.delete(삭제할_노선Id)).doesNotThrowAnyException();
        assertThat(lineDao.findAll()).isEmpty();
    }
}