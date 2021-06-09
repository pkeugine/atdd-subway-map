package wooteco.subway.station;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class StationDao {

    public Station save(Station station) {
        return new Station("피케이역");
    }

    public static List<Station> findAll() {
        return Arrays.asList(
                new Station("피케이역"),
                new Station("우테코역")
        );
    }
}
