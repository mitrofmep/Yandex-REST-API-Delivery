package ru.yandex.yandexlavka.util;

public interface ModelToDtoMapper<I, R> {
    R mapToDto(I model);
}
