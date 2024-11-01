package com.project.getinline.repository.querydsl;

import com.project.getinline.constant.ErrorCode;
import com.project.getinline.constant.EventStatus;
import com.project.getinline.domain.Event;
import com.project.getinline.domain.QEvent;
import com.project.getinline.dto.EventViewResponse;
import com.project.getinline.exception.GeneralException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class EventRepositoryCustomImpl extends QuerydslRepositorySupport implements EventRepositoryCustom{

    public EventRepositoryCustomImpl() {
        super(Event.class);
    }

    @Override
    public Page<EventViewResponse> findEventViewPageBySearchParams(
            String placeName,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Pageable pageable
    ) {
        QEvent event = QEvent.event;

        JPQLQuery<EventViewResponse> query = from(event)
                .select(Projections.constructor(
                        EventViewResponse.class,
                        event.id,
                        event.place.placeName,
                        event.eventName,
                        event.eventStatus,
                        event.eventStartDatetime,
                        event.eventEndDatetime,
                        event.currentNumberOfPeople,
                        event.capacity,
                        event.memo
                ));

        // event의 place의 placeName이 대소문자 상관없이 검색하는 메소드
        if(placeName != null && !placeName.isBlank()){
            query.where(event.place.placeName.containsIgnoreCase(placeName));
        }
        if(eventName != null && !eventName.isBlank()){
            query.where(event.eventName.containsIgnoreCase(eventName));
        }
        if(eventStatus != null ){
            query.where(event.eventStatus.eq(eventStatus));
        }
        // goe : greater than or equal ( >= ) 크거나 같은
        if (eventStartDatetime != null) {
            query.where(event.eventStartDatetime.goe(eventStartDatetime));
        }
        // loe : less than or equal ( <= ) 작거나 같은
        if (eventEndDatetime != null) {
            query.where(event.eventEndDatetime.loe(eventEndDatetime));
        }

        List<EventViewResponse> events = Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new GeneralException(ErrorCode.DATA_ACCESS_ERROR, "Spring Data JPA 로부터 Querydsl 인스턴스를 가져올 수 없다."))
                .applyPagination(pageable, query)
                .fetch();

        // total size는 따로 재서 입력
        return new PageImpl<>(events, pageable, query.fetchCount());
    }
}
