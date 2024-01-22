package com.project.getinline.service;

import com.project.getinline.constant.EventStatus;
import com.project.getinline.dto.EventDTO;
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

    public List<EventDTO> getEvents(
            Long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime
    ){
        return eventRepository.findEvents();
    }

    public Optional<EventDTO> getEvent(Long eventId){
        return Optional.empty();
    }

    public boolean createEvent(EventDTO eventDTO){
        return true;
    }

    public boolean modifyEvent(Long id, EventDTO eventDTO){
        return true;
    }

    public boolean deleteEvent(Long eventId){
        return true;
    }

}
