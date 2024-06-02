package com.project.getinline.repository.querydsl;

import com.project.getinline.constant.EventStatus;
import com.project.getinline.dto.EventViewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface EventRepositoryCustom {
    Page<EventViewResponse> findEventViewPageBySearchParams(
            String placeName,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime,
            Pageable pageable
    );

}
