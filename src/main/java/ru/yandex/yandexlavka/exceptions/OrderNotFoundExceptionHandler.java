package ru.yandex.yandexlavka.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.yandexlavka.dto.NotFoundResponse;

@RestControllerAdvice
public class OrderNotFoundExceptionHandler {
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public NotFoundResponse orderNotFoundException(OrderNotFoundException e) {
        return new NotFoundResponse();
    }
}
