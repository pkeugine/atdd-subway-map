package wooteco.subway.line.ui;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineWithStationsResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.dto.SectionRequest;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineWithStationsResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineWithStationsResponse lineWithStationsResponse = lineService.createLine(lineRequest);
        Long lineId = lineWithStationsResponse.getId();

        return ResponseEntity
                .created(URI.create("/lines/" + lineId))
                .body(lineWithStationsResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> lineResponse = lineService.findAllLineSimpleResponses();

        return ResponseEntity
                .ok(lineResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineWithStationsResponse> showLine(@PathVariable Long id) {
        LineWithStationsResponse lineWithStationsResponse = lineService.findLineResponseById(id);

        return ResponseEntity
                .ok(lineWithStationsResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> addSectionInLine(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        // TODO : 구간 추가 구현

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSectionInLine(@PathVariable Long id, @RequestParam Long stationId) {
        // TODO : 구간 삭제 구현

        return ResponseEntity
                .noContent()
                .build();
    }
}
