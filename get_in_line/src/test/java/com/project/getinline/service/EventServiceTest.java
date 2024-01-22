package com.project.getinline.service;

import com.project.getinline.constant.EventStatus;
import com.project.getinline.dto.EventDTO;
import com.project.getinline.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks private EventService sut;
    @Mock private EventRepository eventRepository;

    /*
    // 구현체
    @BeforeEach
    void setUp(){
        sut = new EventService();
    }
    @ExtendWith(MockitoExtension.class) 사용해서 필요없어짐
     */

    @DisplayName("검색 조건 없이 이벤트 검색하면, 전체 결과를 출력하여 보여준다.")
    @Test
    void givenNothing_whenSearchingEvents_thenReturnsEntireEventList() {
        // given
        given(eventRepository.findEvents(null, null, null, null, null))
                .willReturn(List.of(
                        createEventDTO(1L, "오전 운동", true),
                        createEventDTO(1L, "오후 운동", false)
                ));

        // when
        List<EventDTO> list = sut.getEvents(null, null, null,
                null, null);

        // then
        assertThat(list).hasSize(2);
        then(eventRepository).should().findEvents(null, null, null,
                null, null);
    }

    @DisplayName("검색 조건과 함꼐 이벤트 검색하면, 전체 결과를 출력하여 보여준다.")
    @Test
    void givenSearchParams_whenSearchingEvents_thenReturnsReturnsEventList() {
        // given
        Long placeId = 1L;
        String eventName = "오전 운동";
        EventStatus eventStatus = EventStatus.OPENED;
        LocalDateTime eventStartDatetime = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        LocalDateTime eventEndDatetime = LocalDateTime.of(2021, 1, 2, 0, 0, 0);

        given(eventRepository.findEvents(placeId,eventName,eventStatus,eventStartDatetime,eventEndDatetime))
                .willReturn(List.of(createEventDTO(1L, "오전 운동", eventStatus, eventStartDatetime,eventEndDatetime )));

        // when
        List<EventDTO> list = sut.getEvents(
                placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime
        );

        // then
        assertThat(list)
                .hasSize(1)
                .allSatisfy(event -> {
                    assertThat(event)
                            .hasFieldOrPropertyWithValue("placeId", placeId)
                            .hasFieldOrPropertyWithValue("eventName", eventName)
                            .hasFieldOrPropertyWithValue("eventStatus", eventStatus);
                    assertThat(event.eventStartDatetime()).isAfterOrEqualTo(eventStartDatetime);
                    assertThat(event.eventStartDatetime()).isBeforeOrEqualTo(eventEndDatetime);
                });
        then(eventRepository).should().findEvents(placeId,eventName,eventStatus,eventStartDatetime,eventEndDatetime);
    }

    @DisplayName("이벤트 ID로 존재하는 이벤트를 조회하면, 해당 이벤트 정보를 출력하여 보여준다")
    @Test
    void givenEventId_whenSearchingExistingEvent_thenReturnsEvent(){
        // given
        long eventId = 1L;
        EventDTO eventDTO = createEventDTO(1L, "오전 운동", true);
        given(eventRepository.findEvent(eventId)).willReturn(Optional.of(eventDTO));

        // when
        Optional<EventDTO> result = sut.getEvent(eventId);

        // then
        assertThat(result).hasValue(eventDTO);
        then(eventRepository).should().findEvent(eventId);
    }

    @DisplayName("이벤트 ID로 존재하는 이벤트를 조회하면, 빈 정보를 출력하여 보여준다.")
    @Test
    void givenEventId_whenSearchingNonexistentEvent_thenReturnsEmptyOptional(){
        // given
        long eventId = 2L;
        given(eventRepository.findEvent(eventId)).willReturn(Optional.empty());

        // when
        Optional<EventDTO> result = sut.getEvent(eventId);

        // then
        assertThat(result).isEmpty();
        then(eventRepository).should().findEvent(eventId);
    }

    @DisplayName("이벤트 정보를 주면, 이벤트를 생성하고 결과를 true 로 보여준다")
    @Test
    void givenEvent_whenCreating_thenCreatesEventAndReturnsTrue(){
        // given
        EventDTO dto = createEventDTO(1L,"오후 운동", false);

        // when
        boolean result = sut.createEvent(dto);

        // then
        assertThat(result).isTrue();

    }

    @DisplayName("이벤트 정보를 주지 않으면, 생성 중단하고 결과를 false 로 보여준다")
    @Test
    void givenNothing_whenCreating_thenAbortCreatingAndReturnsFalse(){
        // given


        // when
        boolean result = sut.createEvent(null);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("이벤트 ID와 정보를 주면, 이벤트 정보를 변경하고 결과를 true 로 보여준다")
    @Test
    void givenEventIdAndItsInfo_whenModifying_thenModifiesAndReturnsTrue(){
        // given
        long eventId = 1L;
        EventDTO dto = createEventDTO(1L, "오후 운동", false);

        // when
        boolean result = sut.modifyEvent(eventId, dto);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("이벤트 ID를 주지 않으면, 이벤트 정보 변경을 중단하고 결과를 false 로 보여준다")
    @Test
    void givenNoEventId_whenModifying_thenAbortModifyingAndReturnsFalse(){
        // given
        EventDTO dto = createEventDTO(1L, "오후 운동", false);

        // when
        boolean result = sut.modifyEvent(null, dto);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("이벤트 ID만 주고 변경할 정보를 주지 않으면, 이벤트 정보 변경 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenEventIdOnly_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // Given
        long eventId = 1L;

        // When
        boolean result = sut.modifyEvent(eventId, null);

        // Then
        assertThat(result).isFalse();
    }

    @DisplayName("이벤트 ID를 주면, 이벤트 정보를 삭제하고 결과를 true 로 보여준다")
    @Test
    void givenEventId_whenDeleting_thenDeletesEventAndReturnTrue(){
        // given
        long eventId = 1L;

        // when
        boolean result = sut.deleteEvent(eventId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("이벤트 ID를 주지 않으면, 삭제 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenDeleting_thenAbortsDeletingAndReturnsFalse() {
        // given


        // when
        boolean result = sut.deleteEvent(null);

        // then
        assertThat(result).isFalse();
    }

    private EventDTO createEventDTO(long placeId, String eventName, boolean isMorning){
        String hourStart = isMorning ? "09" : "13";
        String hourEnd = isMorning ? "12":"16";

        return createEventDTO(
                placeId,
                eventName,
                EventStatus.OPENED,
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourStart)),
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourEnd))
        );
    }

    private EventDTO createEventDTO(
            long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime
    ){
        return EventDTO.of(
                placeId,
                eventName,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                0,
                24,
                "마스크 꼭 착용하세요",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}