package com.project.getinline.controller.api;

import com.project.getinline.dto.APIDataResponse;
import com.project.getinline.dto.AdminRequest;
import com.project.getinline.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
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
