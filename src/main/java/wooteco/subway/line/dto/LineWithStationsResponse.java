package wooteco.subway.line.dto;

import java.util.Collections;
import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

public class LineWithStationsResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineWithStationsResponse(Long id, String name, String color) {
        this(id, name, color, Collections.emptyList());
    }

    public LineWithStationsResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineWithStationsResponse of(Line line) {
        return new LineWithStationsResponse(line.getId(), line.getName(), line.getColor());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
