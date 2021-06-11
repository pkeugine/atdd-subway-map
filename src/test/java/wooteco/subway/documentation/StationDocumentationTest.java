package wooteco.subway.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.StationDuplicateException;
import wooteco.subway.station.ui.StationController;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(StationController.class)
class StationDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationService stationService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("지하철 역 생성 - 성공")
    @Test
    void createStation_success() throws Exception {
        // given
        final StationRequest 피케이역_요청 = new StationRequest("피케이역");
        final StationResponse 피케이역_응답 = new StationResponse(1L, "피케이역");

        given(stationService.createStation(any(StationRequest.class))).willReturn(피케이역_응답);

        // when
        final ResultActions result = this.mockMvc.perform(
                post("/stations")
                        .content(objectMapper.writeValueAsString(피케이역_요청))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        result.andExpect(status().isCreated())
                .andDo(document("station-post-success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("역 이름")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("역 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("역 이름")
                        )
                ));
    }

    @DisplayName("지하철 역 생성 - 실패, 중복되는 역 존재")
    @Test
    void createStation_fail_duplicateName() throws Exception {
        // given
        final StationRequest 피케이역_요청 = new StationRequest("피케이역");
        final StationResponse 피케이역_응답 = new StationResponse(1L, "피케이역");

        given(stationService.createStation(any(StationRequest.class)))
                .willThrow(new StationDuplicateException(피케이역_응답.getName()));

        // when
        final ResultActions result = this.mockMvc.perform(
                post("/stations")
                        .content(objectMapper.writeValueAsString(피케이역_요청))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        result.andExpect(status().isBadRequest())
                .andDo(document("station-post-fail-duplicate-name",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("역 이름")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("에러 메시지")
                        )
                ));
    }


    @DisplayName("지하철 역 전체 조회")
    @Test
    void showStations_success() throws Exception {
        // given
        given(stationService.showStations()).willReturn(Arrays.asList(
                new StationResponse(1L, "피케이역"),
                new StationResponse(2L, "코지역")
        ));

        // when
        ResultActions result = this.mockMvc.perform(
                get("/stations")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        FieldDescriptor[] station = new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("역 id"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("역 이름")};
        result.andExpect(status().isOk())
                .andDo(document("station-get-success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[]").description("저장된 모든 역"))
                                .andWithPrefix("[].", station)
                ));
    }

    @DisplayName("지하철 역 삭제 - 성공")
    @Test
    void deleteStation_success() throws Exception {
        // given // when
        final ResultActions result = this.mockMvc.perform(
                delete("/stations/1")
        );

        // then
        result.andExpect(status().isNoContent())
                .andDo(document("station-delete-success",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }

}
