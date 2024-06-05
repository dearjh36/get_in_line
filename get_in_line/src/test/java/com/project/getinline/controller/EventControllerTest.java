package com.project.getinline.controller;

import com.project.getinline.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 이벤트")
@WebMvcTest(EventController.class)
class EventControllerTest {

    private final MockMvc mvc;

    @MockBean private EventService eventService;

    public EventControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 이벤트 리스트 페이지")
    @Test
    void givenNothing_whenRequestingEventsPage_thenReturnsEventsPage() throws Exception {
        // Given
        given(eventService.getEvents(any())).willReturn(List.of());

        // When & Then
        mvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/index"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("events"));

        then(eventService).should().getEvents(any());
    }

    @DisplayName("[view][GET] 이벤트 리스트 페이지 - 커스텀 페이지")
    @Test
    void givenNothing_whenRequestingCustomEventsPage_thenReturnsEventsPage() throws Exception{
        // Given
        given(eventService.getEventViewResponse(any(), any(), any(), any(), any(), any())).willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/events/custom"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("evnet/index"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("events"));
        then(eventService).should().getEventViewResponse(any(), any(), any(), any(), any(), any());;

    }

    @DisplayName("[view][GET] 이벤트 세부 정보 페이지")
    @Test
    void givenNothing_whenRequestingEventDetailPage_thenReturnsEventDetailPage() throws Exception {
        // Given
        long eventId = 1L;

        // When & Then
        mvc.perform(get("/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/detail"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("event"))
        ;
    }

}
