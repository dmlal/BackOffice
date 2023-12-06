package com.sparta.backoffice.global.aop;

import com.sparta.backoffice.global.dto.BaseResponse;
import com.sparta.backoffice.global.exception.ApiException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.sparta.backoffice.global.constant.ErrorCode.*;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * [Exception] RuntimeException 반환하는 경우
	 *
	 * @param ex RuntimeException
	 * @return ResponseEntity<BaseResponse>
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<BaseResponse<Void>> runtimeExceptionHandler(RuntimeException ex) {
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

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<BaseResponse<Object>> nullPointerExceptionHandler(NullPointerException ex) {
        log.error("NullPointerExceptions :", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        BaseResponse.of(
                                ex.getMessage(),
                                HttpStatus.NOT_FOUND.value(),
                                null
                        )
                );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<BaseResponse<Object>> ArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("ArgeumentNotValidExceptions :", message);
        return ResponseEntity.badRequest()
                .body(
                        BaseResponse.of(
                                message,
                                HttpStatus.BAD_REQUEST.value(),
                                null
                        )
                );
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<BaseResponse<Object>> IllegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.error("IllegalArgumentException :", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(
                        BaseResponse.of(
                                ex.getMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                null
                        )
                );
    }

	/**
	 * [Exception] API 요청 시 '객체' 혹은 '파라미터' 데이터 값이 유효하지 않은 경우
	 *
	 * @param ex MethodArgumentNotValidException,
	 * @return ResponseEntity<BaseResponse>
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error("handleMethodArgumentNotValidException", ex);
		BindingResult bindingResult = ex.getBindingResult();
		HashMap<String, String> errors = new HashMap<>();
		bindingResult.getAllErrors()
			.forEach(error -> errors.put(((FieldError)error).getField(), error.getDefaultMessage()));

		return ResponseEntity.badRequest()
			.body(
				BaseResponse.of(
					INVALID_VALUE.getMessage(),
					INVALID_VALUE.getHttpStatus(),
					errors
				)
			);
	}

	/**
	 * [Exception] API 요청에 맞는 파라미터를 받지 못한 경우
	 *
	 * @param ex MissingServletRequestParameterException,
	 * @return ResponseEntity<BaseResponse>
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
			BaseResponse.of(
				"누락된 파라미터 : " + ex.getParameterName(),
				INVALID_VALUE.getHttpStatus(),
				null
			)
		);
	}

	/**
	 * [Exception] ApiException 반환하는 경우
	 * ErrorCode 를 사용하여 공통예외를 반환하는 경우 핸들링
	 *
	 * @param ex ApiException
	 * @return ResponseEntity<BaseResponse>
	 */

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<BaseResponse<Void>> runtimeExceptionHandler(ApiException ex) {
		log.error("Runtime Exceptions :", ex);
		return ResponseEntity.internalServerError()
			.body(
				BaseResponse.of(
					ex.getErrorCode().getMessage(),
					ex.getErrorCode().getHttpStatus(),
					null
				)
			);
	}
}
