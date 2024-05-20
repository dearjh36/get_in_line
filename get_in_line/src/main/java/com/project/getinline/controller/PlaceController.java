package com.project.getinline.controller;

import com.project.getinline.domain.Place;
import com.project.getinline.dto.PlaceResponse;
import com.project.getinline.service.PlaceService;
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
import java.util.function.Predicate;

@RequiredArgsConstructor
@RequestMapping("/places")
@Controller
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public ModelAndView places(@QuerydslPredicate(root = Place.class) Predicate predicate){
        Map<String, Object> map = new HashMap<>();
        List<PlaceResponse> places = placeService.getPlaces(predicate)
                .stream()
                .map(PlaceResponse :: from)
                .toList();
        map.put("places", places);

        return new ModelAndView("place/index", map);
    }

    @GetMapping("/{placeId}")
    public ModelAndView placeDetail(@PathVariable Long placeId){
        Map<String, Object> map = new HashMap<>();
    }

}
