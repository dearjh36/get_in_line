package com.project.getinline.dto;

import com.project.getinline.constant.EventStatus;

import java.time.LocalDateTime;

public record EventViewResponse(
        Long id,
        // EventResponse의 PlaceDto는 정보가 많으니 이름만으로 검색하는 EventViewResponse 생성
        String placeName,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDatetime,
        LocalDateTime eventEndDatetime,
        Integer currentNumberOfPeople,
        Integer capacity,
        String memo

) {
    public EventViewResponse(Long id, String placeName, String eventName, EventStatus eventStatus, LocalDateTime eventStartDatetime, LocalDateTime eventEndDatetime, Integer currentNumberOfPeople, Integer capacity, String memo) {
        this.id = id;
        this.placeName = placeName;
        this.eventName = eventName;
        this.eventStatus = eventStatus;
        this.eventStartDatetime = eventStartDatetime;
        this.eventEndDatetime = eventEndDatetime;
        this.currentNumberOfPeople = currentNumberOfPeople;
        this.capacity = capacity;
        this.memo = memo;
    }

    public static EventViewResponse of(
            Long id,
            String placeName,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Integer currentNumberOfPeople,
            Integer capacity,
            String memo
    ) {
        return new EventViewResponse(
                id,
                placeName,
                eventName,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                currentNumberOfPeople,
                capacity,
                memo
        );
    }

    public static EventViewResponse from(EventDto eventDTO) {
        if (eventDTO == null) { return null; }
        return EventViewResponse.of(
                eventDTO.id(),
                eventDTO.placeDto().placeName(),
                eventDTO.eventName(),
                eventDTO.eventStatus(),
                eventDTO.eventStartDatetime(),
                eventDTO.eventEndDatetime(),
                eventDTO.currentNumberOfPeople(),
                eventDTO.capacity(),
                eventDTO.memo()
        );
    }

}
