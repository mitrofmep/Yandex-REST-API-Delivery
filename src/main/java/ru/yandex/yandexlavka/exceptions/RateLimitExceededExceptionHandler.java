package ru.yandex.yandexlavka.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.yandexlavka.dto.BadRequestResponse;

@RestControllerAdvice
public class RateLimitExceededExceptionHandler {
    @ExceptionHandler(RateLimitExceededException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ResponseBody
    public BadRequestResponse rateLimitExceededException(RateLimitExceededException e) {
        return new BadRequestResponse();
    }
}
