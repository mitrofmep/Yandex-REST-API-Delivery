package ru.yandex.yandexlavka.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.yandexlavka.dto.BadRequestResponse;

@RestControllerAdvice
public class MethodArgumentNotValidExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BadRequestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new BadRequestResponse();
    }
}
