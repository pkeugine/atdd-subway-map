package wooteco.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.DuplicateStationException;
import wooteco.subway.station.exception.NotExistingStationException;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public StationResponse createStation(StationRequest stationRequest) {
        try {
            Station station = new Station(stationRequest.getName());
            Station newStation = stationDao.insert(station);
            return new StationResponse(newStation.getId(), newStation.getName());
        } catch (DuplicateKeyException e) {
            throw new DuplicateStationException(stationRequest.getName());
        }
    }

    public List<StationResponse> findAllStationResponses() {
        return findAllStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private List<Station> findAllStations() {
        return stationDao.findAll();
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new NotExistingStationException(id));
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
