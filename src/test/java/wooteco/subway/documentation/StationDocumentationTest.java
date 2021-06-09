package wooteco.subway.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.documentation.ApiDocumentUtils.getDocumentRequest;
import static wooteco.subway.documentation.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.ui.StationController;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.exception.StationDuplicateException;
import wooteco.subway.station.dto.StationRequest;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(StationController.class)
class StationDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationDao stationDao;

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
        final Station 피케이역 = new Station(1L, "피케이역");

        given(stationDao.insert(any(Station.class))).willReturn(피케이역);

        // when
        final StationRequest stationRequest = new StationRequest("피케이역");

        final ResultActions result = this.mockMvc.perform(
                post("/stations")
                        .content(objectMapper.writeValueAsString(stationRequest))
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

    @DisplayName("지하철 역 생성 실패 - 중복되는 역 존재")
    @Test
    void createStation_fail_duplicateName() throws Exception {
        // given
        final Station 피케이역 = new Station(1L, "피케이역");

        given(stationDao.insert(any(Station.class))).willThrow(new StationDuplicateException(피케이역.getName()));

        // when
        final StationRequest stationRequest = new StationRequest("피케이역");

        final ResultActions result = this.mockMvc.perform(
                post("/stations")
                        .content(objectMapper.writeValueAsString(stationRequest))
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
                                fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지")
                        )
                ));
    }

}
