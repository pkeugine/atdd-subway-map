package wooteco.subway.station.exception;

import wooteco.subway.exception.NotExistingException;

public class NotExistingStationException extends NotExistingException {

    public NotExistingStationException(Long id) {
        super(String.format("역이 존재하지 않습니다. 역 id : %d", id));
    }
}
