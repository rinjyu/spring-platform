package com.spring.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"status", "message", "data"})
public class ClientCustomErrorResponseDto<T> {

    private int status;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static ResponseEntity<ClientCustomErrorResponseDto> of(HttpStatus httpStatus, ClientCustomErrorResponseDto clientCustomErrorResponseDto){
        return ResponseEntity
                .status(httpStatus.value())
                .body(clientCustomErrorResponseDto);
    }
}
