package com.project.getinline.dto;

// api가 내보낼 응답을 미리 정한것

import com.project.getinline.constant.ErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class APIDataResponse<T> extends APIErrorResponse {

    private final T data;

    private APIDataResponse(T data){
        super(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }

    public static <T> APIDataResponse<T> of(T data){return new APIDataResponse<>(data);}

    public static <T> APIDataResponse<T> empty(){return new APIDataResponse<>(null)}

}
