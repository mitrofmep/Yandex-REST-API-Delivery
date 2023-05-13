package ru.yandex.yandexlavka.util;

import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.dto.CourierDto;
import ru.yandex.yandexlavka.models.Courier;
import ru.yandex.yandexlavka.models.CourierRegion;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourierToCourierDtoMapper implements ModelToDtoMapper<Courier, CourierDto> {
    @Override
    public CourierDto mapToDto(Courier courier) {
        CourierDto dto = new CourierDto();
        dto.setCourierType(courier.getType().toString());
        dto.setId(courier.getId());

        List<Integer> regionList = new ArrayList<>();
        for (CourierRegion courierRegion : courier.getRegion()) {
            regionList.add(courierRegion.getRegion());
        }
        dto.setRegions(regionList);

        dto.setWorkingHours(courier.getWorkingHour().stream()
                .map(e -> String.format("%s-%s", e.getStart(), e.getFinish())).toList());

        return dto;
    }
}
