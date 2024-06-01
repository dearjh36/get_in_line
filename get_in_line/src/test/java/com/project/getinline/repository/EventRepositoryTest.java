package com.project.getinline.repository;

import com.project.getinline.constant.EventStatus;
import com.project.getinline.constant.PlaceType;
import com.project.getinline.domain.Event;
import com.project.getinline.domain.Place;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    void givenSearch


    @DisplayName("adfa")
    @Test
    void test(){
        // Given
        Place place = createPlace();
        Event event = createEvent(createPlace());
/*        testEntityManager.persist(place);
        testEntityManager.persist(event);*/

        // When
        Iterable<Event> events = eventRepository.findAll(new BooleanBuilder());

        // Then
        assertThat(events).hasSize(1);

    }

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