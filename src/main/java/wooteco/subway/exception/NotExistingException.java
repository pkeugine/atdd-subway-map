package wooteco.subway.exception;

public class NotExistingException extends BadRequestException {

    public NotExistingException(String message) {
        super(message);
    }
}
