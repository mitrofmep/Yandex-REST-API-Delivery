package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderAssignResponse {

    @JsonProperty("date")
    private String date;

    @JsonProperty("couriers")
    private List<CouriersGroupOrders> couriers;
}
