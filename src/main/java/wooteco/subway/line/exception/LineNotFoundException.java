package wooteco.subway.line.exception;

import wooteco.subway.exception.NotFoundException;

public class LineNotFoundException extends NotFoundException {

    public LineNotFoundException(Long lineId) {
        super(String.format("노선이 존재하지 않습니다. 노선 id : %d", lineId));
    }
}
