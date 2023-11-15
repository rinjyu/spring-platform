package com.spring.backend.common.exception;

import com.spring.backend.dto.ClientCustomErrorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ClientCustomException extends RuntimeException {

    private HttpStatus httpStatus;
    private ClientCustomErrorResponseDto clientCustomErrorResponseDto;
}
