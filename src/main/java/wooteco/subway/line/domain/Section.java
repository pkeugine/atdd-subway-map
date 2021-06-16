package wooteco.subway.line.domain;

import wooteco.subway.line.exception.InvalidSectionException;

public class Section {

    private final Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public Section(long lineId, long upStationId, long downStationId, int distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    public Section(Long id, Long lineId, Long upStationId, Long downStationId, int distance) {
        validateStationId(upStationId, downStationId);
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private void validateStationId(Long upStationId, Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new InvalidSectionException("구간의 상행역과 하행역의 id가 동일하여 구간을 생성할 수 없습니다. 중복되는 id : " + upStationId);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
