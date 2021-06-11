package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.station.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
