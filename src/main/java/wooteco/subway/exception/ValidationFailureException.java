package wooteco.subway.exception;

public class ValidationFailureException extends BadRequestException {

    public ValidationFailureException(String message) {
        super(message);
    }
}
