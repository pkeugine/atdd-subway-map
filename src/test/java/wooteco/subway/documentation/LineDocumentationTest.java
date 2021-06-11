package wooteco.subway.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.documentation.ApiDocumentUtils.getDocumentRequest;
import static wooteco.subway.documentation.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineSimpleResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.exception.LineDuplicateException;
import wooteco.subway.line.ui.LineController;
import wooteco.subway.station.dto.StationResponse;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(LineController.class)
class LineDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
    LineResponse 신분당선_응답 = new LineResponse(1L, "신분당선", "bg-red-600", Arrays.asList(
            new StationResponse(1L, "피케이역"),
            new StationResponse(2L, "코지역")
    ));

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("노선 생성 - 성공")
    @Test
    void createLine_success() throws Exception {
        // given
        given(lineService.createLine(any(LineRequest.class))).willReturn(신분당선_응답);

        // when
        ResultActions result = this.mockMvc.perform(
                post("/lines")
                        .content(objectMapper.writeValueAsString(신분당선_요청))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        result.andExpect(status().isCreated())
                .andDo(document("line-post-success",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    @DisplayName("노선 생성 - 실패, 중복되는 노선 이름 존재")
    @Test
    void createLine_fail_duplicatedName() throws Exception {
        // given
        given(lineService.createLine(any(LineRequest.class)))
                .willThrow(new LineDuplicateException("중복되는 이름이 있어서 노선을 추가할 수 없습니다. 노선 이름 : " + 신분당선_요청.getName()));

        // when
        ResultActions result = this.mockMvc.perform(
                post("/lines")
                        .content(objectMapper.writeValueAsString(신분당선_요청))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        result.andExpect(status().isBadRequest())
                .andDo(document("line-post-fail-duplicate-name",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    @DisplayName("노선 생성 - 실패, 중복되는 노선 색상 존재")
    @Test
    void createLine_fail_duplicatedColor() throws Exception {
        // given
        given(lineService.createLine(any(LineRequest.class)))
                .willThrow(new LineDuplicateException("중복되는 색상이 있어서 노선을 추가할 수 없습니다. 노선 색상 : " + 신분당선_요청.getColor()));

        // when
        ResultActions result = this.mockMvc.perform(
                post("/lines")
                        .content(objectMapper.writeValueAsString(신분당선_요청))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        result.andExpect(status().isBadRequest())
                .andDo(document("line-post-fail-duplicate-color",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    @DisplayName("노선 전체 조회")
    @Test
    void showLines_success() throws Exception {
        // given
        given(lineService.findAllLineSimpleResponses()).willReturn(Arrays.asList(
                new LineSimpleResponse(1L, "신분당선", "bg-red-600"),
                new LineSimpleResponse(2L, "2호선", "bg-green-600")
        ));

        // when
        ResultActions result = this.mockMvc.perform(
                get("/lines")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("line-getAll-success",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    @DisplayName("노선 단일 조회")
    @Test
    void showLine_success() throws Exception {
        // given
        given(lineService.findLineResponseById(any(Long.class))).willReturn(신분당선_응답);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/lines/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("line-get-success",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    @DisplayName("노선 수정 - 성공")
    @Test
    void updateLine_success() throws Exception {
        // given
        LineUpdateRequest 신분당선_수정_요청 = new LineUpdateRequest("신분당선", "bg-red-600");

        // when
        ResultActions result = this.mockMvc.perform(
                put("/lines/1")
                        .content(objectMapper.writeValueAsString(신분당선_수정_요청))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("line-put-success",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    @DisplayName("노선 수정 - 실패, 중복되는 이름 또는 색상 존재")
    @Test
    void updateLine_fail_duplicateNameOrColor() throws Exception {
        // given
        LineUpdateRequest 신분당선_수정_요청 = new LineUpdateRequest("신분당선", "bg-red-600");
        doThrow(new LineDuplicateException(신분당선_수정_요청.getName(), 신분당선_수정_요청.getColor()))
                .when(lineService).updateLine(any(Long.class), any(LineUpdateRequest.class));

        // when
        ResultActions result = this.mockMvc.perform(
                put("/lines/1")
                        .content(objectMapper.writeValueAsString(신분당선_수정_요청))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        result.andExpect(status().isBadRequest())
                .andDo(document("line-put-fail-duplicate-name-or-color",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

    @DisplayName("노선 삭제 - 성공")
    @Test
    void deleteLine_success() throws Exception {
        // when
        ResultActions result = this.mockMvc.perform(
                delete("/lines/1")
        );

        // then
        result.andExpect(status().isNoContent())
                .andDo(document("line-delete-success",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }

}
