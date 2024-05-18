package com.project.getinline.controller;

import com.querydsl.core.types.Predicate;
import com.project.getinline.constant.ErrorCode;
import com.project.getinline.domain.Event;
import com.project.getinline.dto.EventResponse;
import com.project.getinline.exception.GeneralException;
import com.project.getinline.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/events")
@Controller
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ModelAndView events(@QuerydslPredicate(root = Event.class) Predicate predicate) {

        Map<String, Object> map = new HashMap<>();

        /*
        // TODO: 임시 데이터. 추후 삭제 예정
        map.put("events", List.of(EventResponse.of(
                    1L,
                    1L,
                    "오후 운동",
                    EventStatus.OPENED,
                    LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                    LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                    0,
                    24,
                    "마스크 꼭 착용하세요"
                ), EventResponse.of(
                    2L,
                    1L,
                    "오후 운동",
                    EventStatus.OPENED,
                    LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                    LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                    0,
                    24,
                    "마스크 꼭 착용하세요"
                )
        ));*/

        List<EventResponse> events = eventService.getEvents(predicate)
                .stream()
                .map(EventResponse::from)
                .toList();

        map.put("events", events);

        return new ModelAndView("event/index", map);
    }

    @GetMapping("/{eventId}")
    public ModelAndView eventDetail(@PathVariable Long eventId) {
        Map<String, Object> map = new HashMap<>();

        /*
        // TODO: 임시 데이터. 추후 삭제 예정
        map.put("event", EventResponse.of(
                eventId,
                1L,
                "오후 운동",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 13, 0, 0),
                LocalDateTime.of(2021, 1, 1, 16, 0, 0),
                0,
                24,
                "마스크 꼭 착용하세요"
        ));*/

        EventResponse event = eventService.getEvent(eventId)
                .map(EventResponse::from)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));

        map.put("event",event);

        return new ModelAndView("event/detail", map);
    }
}
