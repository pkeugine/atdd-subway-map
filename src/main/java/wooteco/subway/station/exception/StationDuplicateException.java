package wooteco.subway.station.exception;

import wooteco.subway.exception.DuplicateException;

public class StationDuplicateException extends DuplicateException {

    public StationDuplicateException(String name) {
        super(String.format("해당 역을 추가하면 역이 중복됩니다. 역 이름 : %s", name));
    }
}
