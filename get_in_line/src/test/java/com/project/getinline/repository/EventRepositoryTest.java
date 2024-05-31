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
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EventRepositoryTest {

    private final EventRepository sut;
    private final TestEntityManager testEntityManager;

    public EventRepositoryTest(
            @Autowired EventRepository sut,
            @Autowired TestEntityManager testEntityManager
    ) {
        this.sut = sut;
        this.testEntityManager = testEntityManager;
    }

    @DisplayName("adfa")
    @Test
    void test(){
        // Given
        Place place = createPlace();
        Event event = createEvent(createPlace());
        testEntityManager.persist(place);
        testEntityManager.persist(event);

        // When
        Iterable<Event> events = sut.findAll(new BooleanBuilder());

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