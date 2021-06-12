package wooteco.subway.line.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import wooteco.subway.station.domain.Station;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;

    public Line(String name, String color) {
        this(null, name, color);
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, Collections.emptyList());
    }

    public Line(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public void addSection(Section section) {
        // TODO 노선에 구간 추가 로직 구현
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name)
                && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
