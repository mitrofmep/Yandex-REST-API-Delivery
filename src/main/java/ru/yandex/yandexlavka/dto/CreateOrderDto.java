package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {
    @JsonProperty("weight")
    @Positive
    private float weight;

    @JsonProperty("regions")
    @Positive
    private int region;

    @JsonProperty("delivery_hours")
    @Valid
    private List<@Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]-(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$") String> deliveryHours;

    @JsonProperty("cost")
    @Positive
    private int cost;
}
