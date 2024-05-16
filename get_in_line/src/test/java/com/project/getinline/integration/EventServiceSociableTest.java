package com.project.getinline.integration;

import com.project.getinline.dto.EventDto;
import com.project.getinline.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EventServiceSociableTest {

    @Autowired private EventService sut;

    @DisplayName("검색 조건 없이 이벤트를 검색하면, 전체 결과를 출력하여 보여준다.")
    @Test
    void givenNothing_whenSearchingEvents_thenReturnsEntireEventList() {
        // Given
        // 실제로 들어갈거기 때문에 모킹 X
//        given(eventRepository.findEvents(null, null, null, null, null))
//                .willReturn(List.of(
//                        createEventDTO(1L, "오전 운동", true),
//                        createEventDTO(1L, "오후 운동", false)
//                ));

        // When
        List<EventDto> list = sut.getEvents(null, null, null, null, null);

        // Then
        assertThat(list).hasSize(0);
        //then(eventRepository).should().findEvents(null, null, null, null, null);
    }

}
