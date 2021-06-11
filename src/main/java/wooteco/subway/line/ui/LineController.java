package wooteco.subway.line.ui;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineSimpleResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.station.dto.StationResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {

        // TODO : URI 경로 하드코딩 수정
        return ResponseEntity
                .created(URI.create("/lines/" + 1))
                .body(new LineResponse(1L, "신분당선", "bg-red-600", Arrays.asList(
                        new StationResponse(1L, "정자역"),
                        new StationResponse(2L, "판교역")
                )));
    }

    @GetMapping
    public ResponseEntity<List<LineSimpleResponse>> showLines() {

        return ResponseEntity
                .ok(Arrays.asList(
                        new LineSimpleResponse(1L, "신분당선", "bg-red-600"),
                        new LineSimpleResponse(2L, "2호선", "bg-green-600")
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {

        return ResponseEntity
                .ok(new LineResponse(id, "신분당선", "bg-red-600", Arrays.asList(
                        new StationResponse(1L, "정자역"),
                        new StationResponse(2L, "판교역")
                )));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest lineUpdateRequest) {

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {

        return ResponseEntity
                .noContent()
                .build();
    }
}
