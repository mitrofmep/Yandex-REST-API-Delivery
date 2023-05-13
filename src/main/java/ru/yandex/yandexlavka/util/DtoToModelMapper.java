package ru.yandex.yandexlavka.util;

public interface DtoToModelMapper<I, R> {
    R mapToModel(I dto);
}
