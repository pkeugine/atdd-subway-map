package wooteco.subway.line.exception;

import wooteco.subway.exception.ValidationFailureException;

public class InvalidSectionException extends ValidationFailureException {

    public InvalidSectionException(String message) {
        super(message);
    }
}
