package ru.yandex.yandexlavka.controllers;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.yandexlavka.dto.CreateCourierDto;
import ru.yandex.yandexlavka.dto.CreateCourierRequest;
import ru.yandex.yandexlavka.dto.CreateCourierResponse;
import ru.yandex.yandexlavka.dto.GetCouriersResponse;
import ru.yandex.yandexlavka.models.Courier;
import ru.yandex.yandexlavka.models.CourierRegion;
import ru.yandex.yandexlavka.models.WorkingHour;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.services.CourierServiceImpl;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourierControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private CourierServiceImpl courierService;

    @AfterEach
    public void resetDb() {
        courierRepository.deleteAll();
    }

    @Test
    public void testCreateCourier_getOkResponseEntity() {
        ResponseEntity<CreateCourierResponse> responseEntity = createCouriers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Long courierId = responseEntity.getBody().getCouriers().get(0).getId();
        Optional<Courier> optionalCourier = courierRepository.findById(courierId);
        assertTrue(optionalCourier.isPresent());

        WorkingHour workingHour1 = new WorkingHour();
        workingHour1.setStart(LocalTime.of(10, 00));
        workingHour1.setFinish(LocalTime.of(14, 00));

        WorkingHour workingHour2 = new WorkingHour();
        workingHour2.setStart(LocalTime.of(16, 30));
        workingHour2.setFinish(LocalTime.of(20, 15));

        Courier courier = optionalCourier.get();
        assertEquals("FOOT", courier.getType().name());
        assertEquals(List.of(1, 2, 3), courier.getRegion().stream().map(CourierRegion::getRegion).collect(Collectors.toList()));
        assertEquals(workingHour1.getStart(), courier.getWorkingHour().get(0).getStart());
        assertEquals(workingHour2.getStart(), courier.getWorkingHour().get(1).getStart());
        assertEquals(workingHour1.getFinish(), courier.getWorkingHour().get(0).getFinish());
        assertEquals(workingHour2.getFinish(), courier.getWorkingHour().get(1).getFinish());
    }

    @Test
    public void whenGetCouriers_thenReturnListOfCouriersWithLimitAndOffset() {
        createCouriers();
        GetCouriersResponse response = courierService.getCouriers(2, 0);

        assertEquals(2, response.getCouriers().size());
        assertEquals(response.getOffset(), 0);
        assertEquals(response.getLimit(), 2);
    }

    private ResponseEntity<CreateCourierResponse> createCouriers() {
        CreateCourierDto createCourierDto1 = new CreateCourierDto();
        createCourierDto1.setCourierType("FOOT");
        createCourierDto1.setRegions(List.of(1, 2, 3));
        createCourierDto1.setWorkingHours(List.of("10:00-14:00", "16:30-20:15"));

        CreateCourierDto createCourierDto2 = new CreateCourierDto();
        createCourierDto2.setCourierType("AUTO");
        createCourierDto2.setRegions(List.of(2, 3));
        createCourierDto2.setWorkingHours(List.of("12:30-15:00", "18:30-22:15"));

        CreateCourierDto createCourierDto3 = new CreateCourierDto();
        createCourierDto3.setCourierType("BIKE");
        createCourierDto3.setRegions(List.of(1, 3));
        createCourierDto3.setWorkingHours(List.of("09:00-12:00", "21:00-02:30"));

        CreateCourierRequest createCourierRequest = new CreateCourierRequest();
        createCourierRequest.setCouriers(List.of(createCourierDto1, createCourierDto2, createCourierDto3));

        return restTemplate.postForEntity("/couriers", createCourierRequest, CreateCourierResponse.class);
    }
}