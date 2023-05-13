package ru.yandex.yandexlavka.util;

import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.dto.CreateCourierDto;
import ru.yandex.yandexlavka.models.Courier;
import ru.yandex.yandexlavka.models.CourierRegion;
import ru.yandex.yandexlavka.models.CourierType;
import ru.yandex.yandexlavka.models.WorkingHour;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateCourierDtoToCourierMapper implements DtoToModelMapper<CreateCourierDto, Courier> {
    @Override
    public Courier mapToModel(CreateCourierDto dto) {

        Courier courier = new Courier();
        List<WorkingHour> workingHours = new ArrayList<>();
        List<String> hoursList = dto.getWorkingHours();
        List<CourierRegion> courierRegions = new ArrayList<>();
        List<Integer> regionsList = dto.getRegions();

        for (String hours : hoursList) {
            String[] hoursParts = hours.split("-");
            WorkingHour workingHour = new WorkingHour();
            LocalTime startHour = LocalTime.parse(hoursParts[0]);
            LocalTime finishHour = LocalTime.parse(hoursParts[1]);
            workingHour.setStart(startHour);
            workingHour.setFinish(finishHour);
            workingHour.setCourier(courier);
            workingHours.add(workingHour);
        }

        courier.setWorkingHour(workingHours);
        courier.setType(CourierType.valueOf(dto.getCourierType()));

        for (Integer region : regionsList) {
            CourierRegion courierRegion = new CourierRegion();
            courierRegion.setCourier(courier);
            courierRegion.setRegion(region);
            courierRegions.add(courierRegion);
        }

        courier.setRegion(courierRegions);

        return courier;
    }
}
