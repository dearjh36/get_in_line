package com.project.getinline.dto;

// api가 내보낼 응답을 미리 정한것

import com.project.getinline.constant.ErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class APIDataResponse extends APIErrorResponse {

    private final Object data;

    private APIDataResponse(boolean success, Integer errorCode, String message, Object data){
        super(success, errorCode, message);
        this.data = data;
    }

    public static APIDataResponse of(boolean success, Integer errorCode, String message, Object data){
        return new APIDataResponse(success, errorCode, message,data);}



}
