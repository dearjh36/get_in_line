package com.project.getinline.dto;

import com.project.getinline.constant.ErrorCode;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
<<<<<<< HEAD
// json body의 일정한 필드들을 보장해서 읽는 쪽에서 검사가 필요하지 않게끔 만들어줌
=======
>>>>>>> c7f4bca792b5ab7dcf24433db1a02af85fbcd50e
public class APIErrorResponse {

    private final Boolean success;
    private final Integer errorCode;
    private final String message;

    public static APIErrorResponse of(Boolean success, Integer errorCode, String message){
        return new APIErrorResponse(success, errorCode, message);
    }

    public static APIErrorResponse of(Boolean success, ErrorCode errorCode){
        return new APIErrorResponse(success, errorCode.getCode(), errorCode.getMessage());
    }

    public static APIErrorResponse of(Boolean success, ErrorCode errorCode, Exception e){
        return new APIErrorResponse(success, errorCode.getCode(), errorCode.getMessage(e));
    }

    public static APIErrorResponse of(Boolean success, ErrorCode errorCode, String message){
        return new APIErrorResponse(success, errorCode.getCode(), errorCode.getMessage(message));
    }

}
