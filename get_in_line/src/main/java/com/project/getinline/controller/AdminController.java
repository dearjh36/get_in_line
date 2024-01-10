package com.project.getinline.controller;

import com.project.getinline.constant.PlaceType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/admin")
@Controller
public class AdminController {

    // @RequestParam 은 생략 가능, 생략하고 안하고 동작차이 있음
    // 생략할시 @RequestParam의 required=false 로 설정
    // 해당 메소드에서는 검색어가 들어가도 되고 안들어가도 되서 생략
    // @RequestParam PlaceType placeType
    @GetMapping("/places")
    public ModelAndView adminPlaces(PlaceType placeType, String placeName, String address){
        Map<String, Object> map = new HashMap<>();
        map.put("placeType", placeType);
        map.put("placeName", placeName);
        map.put("address", address);

        return new ModelAndView("admin/places", map);
    }

    @GetMapping("/places/{placeId}")
    public String adminPlaceDetail(@PathVariable Integer placeId){
        return "admin/place-detail";
    }

    @GetMapping("/events")
    public String adminEvent(){
        return "admin/events";
    }

    @GetMapping("/events/{eventId}")
    public String adminEventDetail(@PathVariable Integer eventId){
        return "admin/event-detail";
    }
}
