package ru.yandex.yandexlavka.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.yandexlavka.dto.BadRequestResponse;

@RestControllerAdvice
public class InvalidCreateCourierRequestExceptionHandler {
    @ExceptionHandler(InvalidCreateCourierRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BadRequestResponse handleInvalidCreateCourierRequestException(InvalidCreateCourierRequestException e) {
        return new BadRequestResponse();
    }
}
