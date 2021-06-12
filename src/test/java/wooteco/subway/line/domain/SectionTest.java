package wooteco.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.application.InvalidSectionException;

class SectionTest {

    @DisplayName("구간 객체 생성 - 성공")
    @Test
    void initialize_success() {
        // given
        Long id = 1L;
        Long lineId = 1L;
        Long upStationId = 1L;
        Long downStationId = 2L;
        int distance = 10;

        // when // then
        assertThatCode(() -> new Section(id, lineId, upStationId, downStationId, distance))
                .doesNotThrowAnyException();
    }

    @DisplayName("구간 객체 생성 - 실패, 상행 종점역과 하행 종점역의 id가 같다")
    @Test
    void initialize_fail_sameStationId() {
        // given
        Long id = 1L;
        Long lineId = 1L;
        Long upStationId = 1L;
        Long downStationId = 1L;
        int distance = 10;

        // when // then
        assertThatThrownBy(() -> new Section(id, lineId, upStationId, downStationId, distance))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessageContaining("id : 1");
    }
}