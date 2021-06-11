package wooteco.subway.line.exception;

import wooteco.subway.exception.DuplicateException;

public class LineDuplicateException extends DuplicateException {

    public LineDuplicateException(String message) {
        super(message);
    }

    public LineDuplicateException(String name, String color) {
        this(String.format("중복되는 노선 이름 또는 색상이 있습니다. 이름: %s, 색상: %s", name, color));
    }
}
