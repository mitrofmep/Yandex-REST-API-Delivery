package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CompleteOrder {
    @JsonProperty("courier_id")
    private long courierId;

    @JsonProperty("order_id")
    private long orderId;

    @JsonProperty("complete_time")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?(Z|[+-]\\d{2}:\\d{2})$")
    private String completeTime;
}
