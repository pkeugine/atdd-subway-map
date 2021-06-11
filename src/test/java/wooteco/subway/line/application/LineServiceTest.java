package wooteco.subway.line.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import wooteco.subway.line.dao.LineDao;

@MockitoSettings
class LineServiceTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineDao lineDao;

    @DisplayName("노선 생성")
    @Test
    void createLine_success() {
        // given

        // when

        // then
    }
}