package wooteco.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineSimpleResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.exception.LineDuplicateException;
import wooteco.subway.line.exception.LineNotFoundException;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Transactional
    public LineResponse createLine(LineRequest lineRequest) {
        validateNoDuplicate(lineRequest.getName(), lineRequest.getColor());
        Line requestedLine = lineRequest.toLine();
        Line createdLine = lineDao.insert(requestedLine);
        return LineResponse.of(createdLine);
    }

    private void validateNoDuplicate(String name, String color) {
        if (lineDao.existsName(name)) {
            throw new LineDuplicateException("중복되는 이름이 있어서 노선을 추가할 수 없습니다. 노선 이름 : " + name);
        }
        if (lineDao.existsColor(color)) {
            throw new LineDuplicateException("중복되는 색상이 있어서 노선을 추가할 수 없습니다. 노선 색상 : " + color);
        }
    }


    public List<LineSimpleResponse> findAllLineSimpleResponses() {
        return findAllLines().stream()
                .map(LineSimpleResponse::of)
                .collect(Collectors.toList());
    }

    private List<Line> findAllLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findLineById(id));
    }

    private Line findLineById(Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        try {
            Line requestedLine = lineUpdateRequest.toLindWithId(id);
            lineDao.update(requestedLine);
        } catch (DuplicateKeyException e) {
            throw new LineDuplicateException(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        }
    }

    public void deleteLineById(Long id) {
        lineDao.delete(id);
    }
}
