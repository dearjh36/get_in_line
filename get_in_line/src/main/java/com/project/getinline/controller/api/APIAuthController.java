package com.project.getinline.controller.api;

import com.project.getinline.dto.APIDataResponse;
import com.project.getinline.dto.AdminRequest;
import com.project.getinline.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

/**
 * Spring Data REST 로 API를 만들어서 당장 필요가 없어진 컨트롤러
 * 우선 deprecated 하고, 향후 사용 방안을 고민해 본다.
 * */

/*
@RequestMapping("/api")
@RestController*/
@Deprecated
public class APIAuthController {
    @PostMapping("/sign-up")
    public APIDataResponse<String> signUp(@RequestBody AdminRequest adminRequest){
        return APIDataResponse.empty();
    }

    @PostMapping("/login")
    public APIDataResponse<String> login(@RequestBody LoginRequest loginRequest){
        return APIDataResponse.empty() ;
    }


}
