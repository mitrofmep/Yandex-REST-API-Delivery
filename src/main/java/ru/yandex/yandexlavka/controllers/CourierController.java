package ru.yandex.yandexlavka.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.dto.*;
import ru.yandex.yandexlavka.services.CourierServiceImpl;

import java.time.LocalDate;

@RestController
@RequestMapping("/couriers")
@RequiredArgsConstructor
@Validated
public class CourierController {
    private final CourierServiceImpl courierService;

    @PostMapping
    @RateLimiter(name = "postCouriers")
    public CreateCourierResponse postCouriers(@RequestBody @Valid CreateCourierRequest createCourierRequest) {
        return courierService.createCouriers(createCourierRequest);
    }

    @GetMapping
    @RateLimiter(name = "getCouriers")
    public GetCouriersResponse getCouriers(@RequestParam(defaultValue = "1", required = false) @Min(0) int limit, @RequestParam(defaultValue = "0", required = false) @Min(0) int offset) {
        return courierService.getCouriers(limit, offset);
    }

    @GetMapping("/{courier_id}")
    @RateLimiter(name = "getCourier")
    public CourierDto getCourier(@PathVariable(name = "courier_id") long courierId) {
        return courierService.getCourier(courierId);
    }

    @GetMapping("/meta-info/{courier_id}")
    @RateLimiter(name = "getCourierMetaInfo")
    public GetCourierMetaInfoResponse getCourierMetaInfo(@PathVariable(name = "courier_id") long courierId,
                                                         @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") @NotNull @Valid LocalDate startDate,
                                                         @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") @NotNull @Valid LocalDate endDate) {
        return courierService.getCourierMetaInfoBetweenStartDateAndEndDate(courierId, startDate, endDate);
    }

    @GetMapping("/assignments")
    @RateLimiter(name = "getCouriersAssignments")
    public OrderAssignResponse getCouriersAssignments(@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid LocalDate date,
                                                      @RequestParam(value = "courier_id", required = false) Long courierId) {
        if (date == null) date = LocalDate.now();
        return courierService.getCouriersAssigns(date, courierId);
    }

}
