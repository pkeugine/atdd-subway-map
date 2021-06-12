package wooteco.subway.line.exception;

import wooteco.subway.exception.NotExistingException;

public class NotExistingLineException extends NotExistingException {

    public NotExistingLineException(Long id) {
        super(String.format("노선이 존재하지 않습니다. 노선 id : %d", id));
    }
}
