package com.spring.backend.common.handler;

import com.spring.backend.common.exception.ClientCustomException;
import com.spring.backend.common.exception.DataNotFoundException;
import com.spring.backend.common.exception.DuplicateDataException;
import com.spring.backend.dto.ClientCustomErrorResponseDto;
import com.spring.backend.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public <T> ResponseDto<List<? super T>> handleBindException(final BindException e) {
        return ResponseDto.<List<? super T>>builder()
                .data((List<T>) e.getBindingResult().getFieldErrors())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("잘못된 요청")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(Optional.ofNullable(e.getMessage()).orElse("잘못된 요청"))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseDto handleDataNotFoundException(final DataNotFoundException e) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Optional.ofNullable(e.getMessage()).orElse("데이터 없음"))
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateDataException.class)
    public ResponseDto handleDuplicateDataException(final DuplicateDataException e) {
        return ResponseDto.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(Optional.ofNullable(e.getMessage()).orElse("데이터 중복"))
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseDto handleException(final Exception e) {
        return ResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(Optional.ofNullable(e.getMessage()).orElse("에러 발생"))
                .build();
    }

    @ExceptionHandler(ClientCustomException.class)
    public ResponseEntity<ClientCustomErrorResponseDto> handleClientCustomException(final ClientCustomException e) {
        return ClientCustomErrorResponseDto.of(e.getHttpStatus(), e.getClientCustomErrorResponseDto());
    }
}
