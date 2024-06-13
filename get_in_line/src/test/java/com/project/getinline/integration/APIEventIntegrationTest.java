package com.project.getinline.integration;

import com.project.getinline.constant.ErrorCode;
import com.project.getinline.constant.EventStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 여러 테스트 방법 사용
@Deprecated
@AutoConfigureMockMvc // mvc 넣어주기 위해 필요, springboot Test는 안넣어줌
@SpringBootTest
public class APIEventIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void asdfg() throws Exception {
        // Given


        // When
        mvc.perform(
                        get("/api/events")
                                .queryParam("placeId", "1")
                                .queryParam("eventName", "운동")
                                .queryParam("eventStatus", EventStatus.OPENED.name())
                                .queryParam("eventStartDatetime", "2021-01-01T00:00:00")
                                .queryParam("eventEndDatetime", "2021-01-02T00:00:00")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()))
                .andDo(print());

        // Then

    }

}
