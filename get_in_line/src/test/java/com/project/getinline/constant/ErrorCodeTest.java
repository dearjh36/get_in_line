package com.project.getinline.constant;

import com.project.getinline.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("에러 처리 - 에러 코드")
class ErrorCodeTest {


    @DisplayName("예외를 받으면, 예외 메시지가 포함된 메시지 출력")
    @Test
    void givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage() {
        // given
        Exception e = new Exception("This is test message.");

        // when
        String result = ErrorCode.OK.getMessage(e);
        // 위의 코드는 Ok 상태에만 관해서 test
        // BAD_REQUEST, SPRING_INTERNAL_ERROR 등 다른 에러사항에 대해 테스트하려면
        String result2 = ErrorCode.SPRING_INTERNAL_ERROR.getMessage(e);
        // 윗줄과 같이 다 작성해야함.
        // 그걸 줄이기 위해 Parameterized Test 사용

        // then
        assertThat(result).isEqualTo("Ok - This is test message.");

    }

    @DisplayName("ParameterizedTest 사용 / 예외를 받으면, 예외 메시지가 포함된 메시지 출력")
    @ParameterizedTest(name = "[{index}] {0} ====> {1}")
    // 입력소스로 메소드 사용한다. 메소드 이름은 사용하는 대상 테스트 이름과 동일하게 작성
    @MethodSource
    void givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage2(ErrorCode sut, String expected) {
        // given
        Exception e = new Exception("This is test message.");

        // when
        String actual = sut.getMessage(e);

        // then
        assertThat(actual).isEqualTo(expected);

    }

    static Stream<Arguments> givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage2() {
        return Stream.of(
                //arguments("input", "output"),
                arguments(ErrorCode.OK, "Ok - This is test message."),
                arguments(ErrorCode.BAD_REQUEST, "Bad request - This is test message."),
                arguments(ErrorCode.SPRING_BAD_REQUEST, "Spring-detected bad request - This is test message."),
                arguments(ErrorCode.VALIDATION_ERROR, "Validation error - This is test message."),
                arguments(ErrorCode.NOT_FOUND, "Requested resource is not found - This is test message."),
                arguments(ErrorCode.INTERNAL_ERROR, "Internal error - This is test message."),
                arguments(ErrorCode.SPRING_INTERNAL_ERROR, "Spring-detected internal error - This is test message."),
                arguments(ErrorCode.DATA_ACCESS_ERROR, "Data access error - This is test message.")
        );
    }

    @DisplayName("에러 메세지를 받으면, 해당 에러 메시지로 출력")
    @ParameterizedTest(name = "[{index}] \"{0}\" ===> \"{1}\"")
    // 입력소스로 메소드 사용한다. 메소드 이름은 사용하는 대상 테스트 이름과 동일하게 작성
    @MethodSource
    void givenMessage_whenGettingMessage_thenReturnsMessage(String input, String expected) {
        // given

        // when
        String actual = ErrorCode.INTERNAL_ERROR.getMessage(input);

        // then
        assertThat(actual).isEqualTo(expected);

    }

    static Stream<Arguments> givenMessage_whenGettingMessage_thenReturnsMessage() {
        return Stream.of(
                arguments(null, ErrorCode.INTERNAL_ERROR.getMessage()),
                arguments("", ErrorCode.INTERNAL_ERROR.getMessage()),
                arguments(" ", ErrorCode.INTERNAL_ERROR.getMessage()),
                arguments("This is test message", "This is test message")

        );
    }

    @DisplayName("toString() 호출 포맷")
    @Test
    void givenErrorCode_whenToString_thenReturnsSimplifiedToString() {
        // Given

        // When
        String result = ErrorCode.INTERNAL_ERROR.toString();

        // Then
        assertThat(result).isEqualTo("INTERNAL_ERROR (20000)");
    }

    @DisplayName("HttpStatus 에 대응하는 ErrorCode 착지 - 정상")
    @MethodSource
    @ParameterizedTest(name = "[{index}] {0} ===> {1}")
    void givenHttpStatus_whenGettingErrorCode_thenReturnsErrorCode(HttpStatus httpStatus, ErrorCode expected){
        // Given


        // When
        ErrorCode actual = ErrorCode.valueOf(httpStatus);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> givenHttpStatus_whenGettingErrorCode_thenReturnsErrorCode() {
        return Stream.of(
                // 정의된 값
                arguments(HttpStatus.OK, ErrorCode.OK),
                arguments(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST),
                arguments(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR),

                // 정의되지 않은 값
                arguments(HttpStatus.CONTINUE, ErrorCode.OK),
                arguments(HttpStatus.ACCEPTED, ErrorCode.OK),
                arguments(HttpStatus.MULTI_STATUS, ErrorCode.OK),
                arguments(HttpStatus.MOVED_PERMANENTLY, ErrorCode.OK),
                arguments(HttpStatus.CONFLICT, ErrorCode.BAD_REQUEST),
                arguments(HttpStatus.EXPECTATION_FAILED, ErrorCode.BAD_REQUEST),
                arguments(HttpStatus.BAD_GATEWAY, ErrorCode.INTERNAL_ERROR),
                arguments(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, ErrorCode.INTERNAL_ERROR)
        );
    }

    @DisplayName("HttpStatus 에 대응하는 ErrorCode 찾기 - Null 처리")
    @Test
    void givenUnknownHttpStatus_whenGettingErrorCode_thenReturnsErrorCode(){
        // Given
        HttpStatus nullStatus = null;

        // When
        Throwable t = catchThrowable(() -> ErrorCode.valueOf(nullStatus));

        // Then
        assertThat(t)
                .isInstanceOf(GeneralException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INTERNAL_ERROR)
                .hasMessage("HttpStatus is null");

    }

}