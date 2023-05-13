package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class GroupOrders {
    @JsonProperty("group_order_id")
    private long id;

    @JsonProperty("orders")
    @Valid
    private List<OrderDto> orders;
}
