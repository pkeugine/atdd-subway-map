package wooteco.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.exception.DuplicateLineException;

@MockitoSettings
class LineServiceTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineDao lineDao;

    @DisplayName("노선 생성 - 성공")
    @Test
    void createLine_success() {
        // given
        LineRequest 노선추가_요청 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
        Line 추가된_노선 = new Line(1L, "추가된노선", "bg-red-600");
        given(lineDao.insert(any(Line.class))).willReturn(추가된_노선);

        // when
        lineService.createLine(노선추가_요청);

        // then
        then(lineDao).should(times(1)).insert(any(Line.class));
    }

    @DisplayName("노선 생성 - 실패, 중복되는 이름 존재")
    @Test
    void createLine_fail_duplicatedName() {
        // given
        LineRequest 노선추가_요청 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
        given(lineDao.existsName(any(String.class)))
                .willThrow(DuplicateLineException.class);

        // when // then
        assertThatThrownBy(() -> lineService.createLine(노선추가_요청))
                .isInstanceOf(DuplicateLineException.class);
        then(lineDao).should(never()).insert(any(Line.class));
    }

    @DisplayName("노선 생성 - 실패, 중복되는 색상 존재")
    @Test
    void createLine_fail_duplicatedColor() {
        // given
        LineRequest 노선추가_요청 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
        given(lineDao.existsColor(any(String.class)))
                .willThrow(DuplicateLineException.class);

        // when // then
        assertThatThrownBy(() -> lineService.createLine(노선추가_요청))
                .isInstanceOf(DuplicateLineException.class);
        then(lineDao).should(never()).insert(any(Line.class));
    }

    @DisplayName("전체 노선 조회 후 SimpleResponse 리스트 생성 - 성공")
    @Test
    void findAllLineSimpleResponses() {
        // given
        List<Line> lines = Arrays.asList(
                new Line(1L, "신분당선", "bg-red-600"),
                new Line(2L, "2호선", "bg-green-600")
        );
        given(lineDao.findAll()).willReturn(lines);

        // when
        List<LineResponse> lineRespons = lineService.findAllLineSimpleResponses();

        // then
        assertThat(lineRespons).usingRecursiveComparison()
                .isEqualTo(lines);
        then(lineDao).should(times(1)).findAll();
    }
}