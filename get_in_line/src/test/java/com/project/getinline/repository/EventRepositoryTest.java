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
        testEntityManager.persist(createEvent());

        // When
        Iterable<Event> events = sut.findAll(new BooleanBuilder());

        // Then
        assertThat(events).hasSize(1);

    }

    private Event createEvent(){
        return createEvent(1L, 1L, "test event", EventStatus.ABORTED, LocalDateTime.now(),LocalDateTime.now());
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

    private Place createPlace(long placeId) {
        Place place = Place.of(PlaceType.COMMON,"test place", "test address", "01012345678",1," ");
        ReflectionTestUtils.setField(place, "id", 1L);

        return place;
    }
}