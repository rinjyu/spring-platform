package com.spring.backend.common.handler;

import com.spring.backend.common.exception.ClientCustomException;
import com.spring.backend.dto.ClientCustomErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    private List<HttpMessageConverter<?>> messageConverters = Arrays.asList(new MappingJackson2HttpMessageConverter());

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return (clientHttpResponse.getStatusCode().series() == CLIENT_ERROR
                || clientHttpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) {
        HttpMessageConverterExtractor<ClientCustomErrorResponseDto> errorMessageExtractor =
                new HttpMessageConverterExtractor(ClientCustomErrorResponseDto.class, messageConverters);
        try {
            ClientCustomErrorResponseDto clientCustomErrorResponseDto = errorMessageExtractor.extractData(clientHttpResponse);
            throw new ClientCustomException(clientHttpResponse.getStatusCode(), clientCustomErrorResponseDto);
        } catch (IOException e) {
            log.error("RestTemplateResponseHandler ::: error = {}", e.getMessage());
        }
    }
}
