package com.project.getinline.service;

import com.project.getinline.constant.ErrorCode;
import com.project.getinline.constant.EventStatus;
import com.project.getinline.constant.PlaceType;
import com.project.getinline.domain.Event;
import com.project.getinline.domain.Place;
import com.project.getinline.dto.EventDto;
import com.project.getinline.exception.GeneralException;
import com.project.getinline.repository.EventRepository;
import com.project.getinline.repository.PlaceRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 이벤트")
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService sut;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private PlaceRepository placeRepository;

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
        // Given
        given(eventRepository.findAll(any(Predicate.class)))
                .willReturn(List.of(
                        createEvent("오전 운동", true),
                        createEvent("오후 운동", false)
                ));

        // When
        List<EventDto> list = sut.getEvents(new BooleanBuilder());

        // Then
        assertThat(list).hasSize(2);
        then(eventRepository).should().findAll(any(Predicate.class));
    }

/* 해당 테스트 상세하게 나눔
    @DisplayName("검색 조건과 함꼐 이벤트 검색하면, 전체 결과를 출력하여 보여준다.")
    @Test
    void givenSearchParams_whenSearchingEvents_thenReturnsReturnsEventList() {
        // given
        Long placeId = 1L;
        String eventName = "오전 운동";
        EventStatus eventStatus = EventStatus.OPENED;
        LocalDateTime eventStartDatetime = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        LocalDateTime eventEndDatetime = LocalDateTime.of(2021, 1, 2, 0, 0, 0);

        given(eventRepository.findEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime))
                .willReturn(List.of(createEventDTO(1L, "오전 운동", eventStatus, eventStartDatetime, eventEndDatetime)));

        // when
        List<EventDto> list = sut.getEvents(
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
        then(eventRepository).should().findEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime);
    }*/

    @DisplayName("이벤트 뷰 데이터를 검색하면, 페이징된 결과를 출력하여 보여준다.")
    @Test
    void givenNothing_whenSearchingEventViewResonse_thenReturnsEventViewResponsePage(){

    }

    @DisplayName("이벤트를 검색하는데 에러가 발생한 경우, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenSearchingEvents_thenThrowsGeneralException() {
        // Given
        RuntimeException e = new RuntimeException("This is test.");
        given(eventRepository.findAll(any(Predicate.class))).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> sut.getEvents(new BooleanBuilder()));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(eventRepository).should().findAll(any(Predicate.class));
    }

    @DisplayName("이벤트 ID로 존재하는 이벤트를 조회하면, 해당 이벤트 정보를 출력하여 보여준다")
    @Test
    void givenEventId_whenSearchingExistingEvent_thenReturnsEvent() {
        // given
        long eventId = 1L;
        Event event = createEvent("오전 운동", true);
        given(eventRepository.findById(eventId)).willReturn(Optional.of(event));

        // when
        Optional<EventDto> result = sut.getEvent(eventId);

        // then
        assertThat(result).hasValue(EventDto.of(event));
        then(eventRepository).should().findById(eventId);
    }

    @DisplayName("이벤트 ID로 이벤트를 조회하면, 빈 정보를 출력하여 보여준다.")
    @Test
    void givenEventId_whenSearchingNonexistentEvent_thenReturnsEmptyOptional() {
        // given
        long eventId = 2L;
        given(eventRepository.findById(eventId)).willReturn(Optional.empty());

        // When
        Optional<EventDto> result = sut.getEvent(eventId);

        // Then
        assertThat(result).isEmpty();
        then(eventRepository).should().findById(eventId);
    }

    @DisplayName("이벤트 ID로 이벤트를 조회하는데 데이터 관련 에러가 발생한 경우, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenSearchingEvent_thenThrowsGeneralException() {
        // Given
        RuntimeException e = new RuntimeException("This is test.");
        given(eventRepository.findById(any())).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> sut.getEvent(null));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(eventRepository).should().findById(any());
    }

    @DisplayName("이벤트 정보를 주면, 이벤트를 생성하고 결과를 true 로 보여준다")
    @Test
    void givenEvent_whenCreating_thenCreatesEventAndReturnsTrue() {
        // given
        EventDto dto = EventDto.of(createEvent("오후 운동", false));
        given(placeRepository.findById(dto.placeDto().id())).willReturn(Optional.of(createPlace()));
        given(eventRepository.save(any(Event.class))).willReturn(any());

        // when
        boolean result = sut.createEvent(dto);

        // then
        assertThat(result).isTrue();
        then(placeRepository).should().findById(dto.placeDto().id());
        then(eventRepository).should().save(any(Event.class));

    }

    @DisplayName("이벤트 정보를 주지 않으면, 생성 중단하고 결과를 false 로 보여준다")
    @Test
    void givenNothing_whenCreating_thenAbortCreatingAndReturnsFalse() {
        // given

        // when
        boolean result = sut.createEvent(null);

        // then
        assertThat(result).isFalse();
        then(placeRepository).shouldHaveNoInteractions();
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 생성 중 데이터 예외가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다")
    @Test
    void givenDataRelatedException_whenCreating_thenThrowsGeneralException() {
        // Given
        Event event = createEvent(null, false);
        RuntimeException e = new RuntimeException("This is test.");
      //  given(eventRepository.insertEvent(any())).willThrow(e);
        given(placeRepository.findById(event.getPlace().getId())).willReturn(Optional.of(createPlace()));
        given(eventRepository.save(any())).willThrow(e);

        // When
        //Throwable thrown = catchThrowable(() -> sut.createEvent(null));
        Throwable thrown = catchThrowable(() -> sut.createEvent(EventDto.of(event)));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        //then(eventRepository).should().insertEvent(any());
        then(eventRepository).should().findById(event.getPlace().getId());
        then(eventRepository).should().save(any());
    }

    @DisplayName("이벤트 ID와 정보를 주면, 이벤트 정보를 변경하고 결과를 true 로 보여준다")
    @Test
    void givenEventIdAndItsInfo_whenModifying_thenModifiesAndReturnsTrue() {
        // given
        long eventId = 1L;
        //EventDto dto = createEventDTO(1L, "오후 운동", false);
        Event originalEvent = createEvent("오후 운동", false);
        Event changedEvent = createEvent("오전 운동", true);
       // given(eventRepository.updateEvent(eventId, dto)).willReturn(true);
        given(eventRepository.findById(eventId)).willReturn(Optional.of(originalEvent));
        given(eventRepository.save(changedEvent)).willReturn(changedEvent);

        // when
        // boolean result = sut.modifyEvent(eventId, dto);
        boolean result = sut.modifyEvent(eventId, EventDto.of(changedEvent));

        // then
        assertThat(result).isTrue();
        //then(eventRepository).should().updateEvent(eventId, dto);
        assertThat(originalEvent.getEventName()).isEqualTo(changedEvent.getEventName());
        assertThat(originalEvent.getEventStartDatetime()).isEqualTo(changedEvent.getEventStartDatetime());
        assertThat(originalEvent.getEventEndDatetime()).isEqualTo(changedEvent.getEventEndDatetime());
        assertThat(originalEvent.getEventStatus()).isEqualTo(changedEvent.getEventStatus());
        then(eventRepository).should().findById(eventId);
        then(eventRepository).should().save(changedEvent);
    }

    @DisplayName("이벤트 ID를 주지 않으면, 이벤트 정보 변경을 중단하고 결과를 false 로 보여준다")
    @Test
    void givenNoEventId_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // given
        Event event = createEvent("오후 운동", false);

        // When
        boolean result = sut.modifyEvent(null, EventDto.of(event));

        // Then
        assertThat(result).isFalse();
        then(eventRepository).shouldHaveNoInteractions();
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
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 변경 중 데이터 오류가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenModifying_thenThrowsGeneralException() {
        // Given
        long eventId = 1L;
        Event originalEvent = createEvent("오후 운동", false);
        Event wrongEvent = createEvent(null, false);
        RuntimeException e = new RuntimeException("This is test.");
        given(eventRepository.findById(eventId)).willReturn(Optional.of(originalEvent));
        given(eventRepository.save(any())).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> sut.modifyEvent(eventId, EventDto.of(wrongEvent)));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(eventRepository).should().findById(eventId);
        then(eventRepository).should().save(any());
    }


    @DisplayName("이벤트 ID를 주면, 이벤트 정보를 삭제하고 결과를 true 로 보여준다")
    @Test
    void givenEventId_whenDeleting_thenDeletesEventAndReturnTrue() {
        // given
        long eventId = 1L;
        willDoNothing().given(eventRepository).deleteById(eventId);

        // when
        boolean result = sut.removeEvent(eventId);

        // then
        assertThat(result).isTrue();
        then(eventRepository).should().deleteById(eventId);
    }

    @DisplayName("이벤트 ID를 주지 않으면, 삭제 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenDeleting_thenAbortsDeletingAndReturnsFalse() {
        // given

        // when
        boolean result = sut.removeEvent(null);

        // then
        assertThat(result).isFalse();
        then(eventRepository).shouldHaveNoInteractions();
    }

    @DisplayName("이벤트 삭제 중 데이터 오류가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenDeleting_thenThrowsGeneralException() {
        // Given
        long eventId = 0L;
        RuntimeException e = new RuntimeException("This is test.");
        willThrow(e).given(eventRepository).deleteById(eventId);

        // When
        Throwable thrown = catchThrowable(() -> sut.removeEvent(eventId));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(eventRepository).should().deleteById(eventId);
    }

    private Event createEvent(String eventName, boolean isMorning) {
        String hourStart = isMorning ? "09" : "13";
        String hourEnd = isMorning ? "12" : "16";

        return createEvent(
                1L,
                1L,
                eventName,
                EventStatus.OPENED,
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourStart)),
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourEnd))
        );
    }

    private Event createEvent(
            long id,
            long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime
    ) {
        Event event = Event.of(
                createPlace(placeId),
                eventName,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                0,
                24,
                "마스크 꼭 착용하세요"
        );
        ReflectionTestUtils.setField(event, "id", id);

        return event;
    }

    private Place createPlace() {
        return createPlace(1L);
    }

    private Place createPlace(long id) {
        Place place = Place.of(PlaceType.COMMON, "test place", "test address", "010-1234-1234", 10, null);
        ReflectionTestUtils.setField(place, "id", id);

        return place;
    }
}