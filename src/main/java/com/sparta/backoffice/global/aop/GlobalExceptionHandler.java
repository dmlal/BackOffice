package com.sparta.backoffice.global.aop;

import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.global.exception.ApiException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static com.sparta.backoffice.global.constant.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * [Exception] RuntimeException 반환하는 경우
     *
     * @param ex RuntimeException
     * @return ResponseEntity<BaseResponse>
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<Object>> runtimeExceptionHandler(RuntimeException ex) {
        log.error("Runtime Exceptions :", ex);
        return ResponseEntity.internalServerError()
                .body(
                        BaseResponse.of(
                                INTERNAL_SERVER_ERROR.getMessage(),
                                INTERNAL_SERVER_ERROR.getHttpStatus(),
                                null
                        )
                );
    }

    /**
     * [Exception] ApiException 반환하는 경우
     *  ErrorCode 를 사용하여 공통예외를 반환하는 경우 핸들링
     * @param ex ApiException
     * @return ResponseEntity<BaseResponse>
     */

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<BaseResponse<Void>> runtimeExceptionHandler(ApiException ex) {
        log.error("Runtime Exceptions :", ex);
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
            .body(
                BaseResponse.of(
                    ex.getErrorCode().getMessage(),
                    ex.getErrorCode().getHttpStatus(),
                    null
                )
            );
    }
}
