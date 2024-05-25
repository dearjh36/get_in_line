package com.project.getinline.service;

import com.project.getinline.constant.ErrorCode;
import com.project.getinline.constant.EventStatus;
import com.project.getinline.dto.EventDto;
import com.project.getinline.exception.GeneralException;
import com.project.getinline.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;


    public List<EventDto> getEvents(
            Long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime
    ) {
        try {
            return eventRepository.findEvents(
                    placeId,
                    eventName,
                    eventStatus,
                    eventStartDatetime,
                    eventEndDatetime
            );
        }
        catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

<<<<<<< HEAD
    public Optional<EventDto> getEvent(Long eventId) {
=======
    public Optional<EventDTO> getEvent(Long eventId) {
>>>>>>> aa02c42 (#14 Refector)
        try {
            return eventRepository.findEvent(eventId);
        }
        catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

<<<<<<< HEAD
    public boolean createEvent(EventDto eventDTO) {
=======
    public boolean createEvent(EventDTO eventDTO) {
>>>>>>> aa02c42 (#14 Refector)
        try {
            return eventRepository.insertEvent(eventDTO);
        }
        catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

<<<<<<< HEAD
    public boolean modifyEvent(Long eventId, EventDto dto) {
=======
    public boolean modifyEvent(Long eventId, EventDTO dto) {
>>>>>>> aa02c42 (#14 Refector)
        try {
            return eventRepository.updateEvent(eventId, dto);
        }
        catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    public boolean removeEvent(Long eventId) {
        try {
            return eventRepository.deleteEvent(eventId);
        }
        catch (Exception e) {
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }
}
