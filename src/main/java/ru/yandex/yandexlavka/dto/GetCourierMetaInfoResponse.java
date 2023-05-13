package ru.yandex.yandexlavka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetCourierMetaInfoResponse {
    @JsonProperty("courier_id")
    private long courierId;

    @JsonProperty("courier_type")
    private String courierType;

    @JsonProperty("regions")
    private List<Integer> regions;

    @JsonProperty("working_hours")
    private List<String> workingHours;

    @JsonProperty("rating")
    private int rating;
    
    @JsonProperty("earnings")
    private int earnings;
}
