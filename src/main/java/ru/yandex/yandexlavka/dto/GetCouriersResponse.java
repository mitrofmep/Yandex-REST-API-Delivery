package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetCouriersResponse {
    @JsonProperty("couriers")
    private List<CourierDto> couriers;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("offset")
    private int offset;
}
