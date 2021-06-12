package wooteco.subway.line.exception;

import wooteco.subway.exception.DuplicateException;

public class DuplicateLineException extends DuplicateException {

    public DuplicateLineException(String message) {
        super(message);
    }

    public DuplicateLineException(String name, String color) {
        this(String.format("중복되는 노선 이름 또는 색상이 있습니다. 이름: %s, 색상: %s", name, color));
    }
}
