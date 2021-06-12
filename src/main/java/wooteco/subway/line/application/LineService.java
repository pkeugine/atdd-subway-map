package wooteco.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineWithStationsResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.exception.DuplicateLineException;
import wooteco.subway.line.exception.NotExistingLineException;
import wooteco.subway.station.application.StationService;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationService stationService;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationService stationService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }


    @Transactional
    public LineWithStationsResponse createLine(LineRequest lineRequest) {
        validateLineNoDuplicate(lineRequest.getName(), lineRequest.getColor());
        Line requestedLine = lineRequest.toLine();
        Line createdLine = lineDao.insert(requestedLine);

        Section requestedSection = lineRequest.toSectionWithLineId(createdLine.getId());
        Section createdSection = sectionDao.insert(requestedSection);
        createdLine.addSection(createdSection);

        return LineWithStationsResponse.of(createdLine);
    }

    private void validateLineNoDuplicate(String name, String color) {
        if (lineDao.existsName(name)) {
            throw new DuplicateLineException("중복되는 이름이 있어서 노선을 추가할 수 없습니다. 노선 이름 : " + name);
        }
        if (lineDao.existsColor(color)) {
            throw new DuplicateLineException("중복되는 색상이 있어서 노선을 추가할 수 없습니다. 노선 색상 : " + color);
        }
    }

//    private Section createSection(Line line, LineRequest lineRequest) {
//        Long upStationId = lineRequest.getUpStationId();
//        Long downStationId = lineRequest.getDownStationId();
//
//        Long lineId = line.getId();
//        Station upStation = stationService.findStationById(upStationId);
//        Station downStation = stationService.findStationById(downStationId);
//        int distance = lineRequest.getDistance();
//
//        Section requestedSection = new Section(lineId, upStation, downStation, distance);
//        return sectionDao.insert(requestedSection);
//    }

    public List<LineResponse> findAllLineSimpleResponses() {
        return findAllLines().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    private List<Line> findAllLines() {
        return lineDao.findAll();
    }

    public LineWithStationsResponse findLineResponseById(Long id) {
        return LineWithStationsResponse.of(findLineById(id));
    }

    private Line findLineById(Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new NotExistingLineException(id));
    }

    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        try {
            Line requestedLine = lineUpdateRequest.toLindWithId(id);
            lineDao.update(requestedLine);
        } catch (DuplicateKeyException e) {
            throw new DuplicateLineException(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        }
    }

    public void deleteLineById(Long id) {
        lineDao.delete(id);
    }
}
