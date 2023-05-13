package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreateCourierResponse {
    @JsonProperty("couriers")
    private List<CourierDto> couriers;
}
