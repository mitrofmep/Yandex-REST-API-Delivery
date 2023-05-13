package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class CouriersGroupOrders {
    @JsonProperty("courier_id")
    private long courierId;

    @JsonProperty("orders")
    @Valid
    private List<GroupOrders> orders;
}
