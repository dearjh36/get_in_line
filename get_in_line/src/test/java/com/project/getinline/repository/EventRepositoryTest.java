package com.project.getinline.repository;

import com.project.getinline.constant.EventStatus;
import com.project.getinline.constant.PlaceType;
import com.project.getinline.domain.Event;
import com.project.getinline.domain.Place;
import com.project.getinline.dto.EventViewResponse;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DB - 이벤트")
@DataJpaTest
class EventRepositoryTest {

    private final EventRepository eventRepository;

    public EventRepositoryTest(@Autowired EventRepository eventRepository) {
        this.eventRepository = eventRepository;

    }

    @Test
    void givenSearchParams_whenFindingEventViewResponse_thenReturnsEventViewResponsePage(){
        // Given


        // When
        Page<EventViewResponse> eventPage = eventRepository.findEventViewPageBySearchParams(
                "배드민턴",
                "운동1",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 0, 0, 0),
                LocalDateTime.of(2021, 1, 2, 0, 0, 0),
                PageRequest.of(0, 5) // 첫 번재 페이지에 5개 기준으로 자르기
        );

        // Then
        assertThat(eventPage.getTotalPages()).isEqualTo(1); // 전체 페이지 수
        assertThat(eventPage.getNumberOfElements()).isEqualTo(1); // 검색 결과 갯수
        assertThat(eventPage.getContent().get(0))
                .hasFieldOrPropertyWithValue("placeName", "서울 배드민턴장")
                .hasFieldOrPropertyWithValue("eventName", "운동1")
                .hasFieldOrPropertyWithValue("eventStatus", EventStatus.OPENED)
                .hasFieldOrPropertyWithValue("eventStartDatetime", LocalDateTime.of(2021, 1, 1, 9, 0, 0))
                .hasFieldOrPropertyWithValue("eventEndDatetime", LocalDateTime.of(2021, 1, 1, 12, 0, 0));
    }

    /*
    @DisplayName("adfa")
    @Test
    void test(){
        // Given
        Place place = createPlace();
        Event event = createEvent(createPlace());
/*        testEntityManager.persist(place);
        testEntityManager.persist(event);

        // When
        Iterable<Event> events = eventRepository.findAll(new BooleanBuilder());

        // Then
        assertThat(events).hasSize(1);

    }*/

    private Event createEvent(Place place){
        return createEvent(place, "test event", EventStatus.ABORTED, LocalDateTime.now(),LocalDateTime.now());
    }


    private Event createEvent(
            Place place,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime
    ) {
        return Event.of(
                place,
                eventName,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                0,
                24,
                "마스크 꼭 착용하세요"
        );

        // 영속성 컨텍스트에 넣을거라 필요없
        //ReflectionTestUtils.setField(event, "id", id);

    }

    private Place createPlace() {
        return Place.of(PlaceType.COMMON,"test place", "test address", "01012345678",1," ");
        //ReflectionTestUtils.setField(place, "id", 1L);

    }
}