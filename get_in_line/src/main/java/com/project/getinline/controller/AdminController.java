package com.project.getinline.controller;

import com.project.getinline.constant.AdminOperationStatus;
import com.project.getinline.constant.ErrorCode;
import com.project.getinline.constant.EventStatus;
import com.project.getinline.constant.PlaceType;
import com.project.getinline.domain.Event;
import com.project.getinline.domain.Place;
import com.project.getinline.dto.*;
import com.project.getinline.exception.GeneralException;
import com.project.getinline.service.EventService;
import com.project.getinline.service.PlaceService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final EventService eventService;
    private final PlaceService placeService;

    @GetMapping("/places")
    public ModelAndView adminPlaces(@QuerydslPredicate(root = Place.class) Predicate predicate){
        List<PlaceResponse> places = placeService.getPlaces(predicate)
                .stream()
                .map(PlaceResponse::from)
                .toList();

        return new ModelAndView("admin/places",
                Map.of(
                        "places", places,
                        "placeTypeOption", PlaceType.values()
                ));
    }

    @GetMapping("/places/{placeId}")
    public ModelAndView adminPlaceDetail(@PathVariable Long placeId, @PageableDefault Pageable pageable) {
        PlaceResponse place = placeService.getPlace(placeId)
                .map(PlaceResponse :: from)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));
        Page<EventViewResponse> events = eventService.getEvent(placeId, pageable);

        return new ModelAndView(
                "admin/place-detail",
                Map.of(
                        "adminOperationStatus", AdminOperationStatus.MODIFY,
                        "place",place,
                        "events",events,
                        "placeTypeOption", PlaceType.values()
                )
        );
    }

    @GetMapping("/places/new")
    public String newPlace(Model model){
        model.addAttribute("adminOprationStatus", AdminOperationStatus.CREATE);
        model.addAttribute("placeTypeOption", PlaceType.values());

        return "admin/place-detail";
    }

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @PostMapping("/places")
    public String upsertPlace(
            @Valid PlaceRequest placeRequest,
            RedirectAttributes redirectAttributes
    ){
        AdminOperationStatus status = placeRequest.id() != null ? AdminOperationStatus.MODIFY : AdminOperationStatus.CREATE;
        placeService.upsertPlace(placeRequest.toDto());

        redirectAttributes.addFlashAttribute("adminOperationStatus", status);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/places");

        return "redirect:/admim/confirm";
    }

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @GetMapping("/places/{placeId}/delete")
    public String deletePlace(
            @PathVariable Long placeId,
            RedirectAttributes redirectAttributes
    ) {
        placeService.removePlace(placeId);

        redirectAttributes.addFlashAttribute("adminOperationStatus", AdminOperationStatus.DELETE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/places");

        return "redirect:/admin/confirm";
    }

    @GetMapping("/places/{placeId}/newEvent")
    public String newEvent(@PathVariable Long placeId, Model model){
        EventResponse event = placeService.getPlace(placeId)
                .map(EventResponse::empty)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));

        model.addAttribute("adminOperationStatus", AdminOperationStatus.CREATE);
        model.addAttribute("eventStatusOption", EventStatus.values());
        model.addAttribute("event", event);

        return "admin/event-detail";
    }

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @PostMapping("/places/{placeId}/events")
    public String upsertEvent(
            @Valid EventRequest eventRequest,
            @PathVariable Long placeId,
            RedirectAttributes redirectAttributes
    ){
        AdminOperationStatus status = eventRequest.id() != null ? AdminOperationStatus.MODIFY : AdminOperationStatus.CREATE;
        eventService.upsertEvent(eventRequest.toDto(PlaceDto.idOnly(placeId)));

        redirectAttributes.addFlashAttribute("adminOperationStatus",status);
        redirectAttributes.addFlashAttribute("redirectUrl","/admin/place/"+placeId);

        return "redirect:/admin/confirm";
    }

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @GetMapping("/events/{eventid}/delete")
    public String deleteEvent(
            @PathVariable Long eventid,
            RedirectAttributes redirectAttributes
    ){
        eventService.removeEvent(eventid);

        redirectAttributes.addFlashAttribute("adminOperationStatus", AdminOperationStatus.DELETE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/events");

        return "redirect:/admin/comfirm";
    }

    @GetMapping("/events")
    public ModelAndView adminEvents(@QuerydslPredicate(root = Event.class) Predicate predicate){
        List<EventResponse> events = eventService.getEvents(predicate)
                .stream()
                .map(EventResponse::from)
                .toList();

        return new ModelAndView(
                "admin/events",
                Map.of(
                        "events", events,
                        "eventStatusOption", EventStatus.values()
                )
        );
    }

    @GetMapping("/events/{eventId}")
    public ModelAndView adminEventDetail(@PathVariable Long eventId){
        EventResponse event = eventService.getEvent(eventId)
                .map(EventResponse::from)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));

        return new ModelAndView("admin/event-detail",
                Map.of(
                        "adminOperationStatus", AdminOperationStatus.MODIFY,
                        "event", event,
                        "eventStatusOption", EventStatus.values()
                )
        );
    }

    @GetMapping("/confirm")
    public String comfirm(Model model){
        if(!model.containsAttribute("redirectUrl")){
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        return "admin/confirm";
    }
}