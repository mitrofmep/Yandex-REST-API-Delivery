package ru.yandex.yandexlavka.services;

import ru.yandex.yandexlavka.dto.*;

import java.time.LocalDate;

public interface CourierService {

    CreateCourierResponse createCouriers(CreateCourierRequest createCourierRequest);

    GetCouriersResponse getCouriers(int limit, int offset);

    CourierDto getCourier(long courierId);

    GetCourierMetaInfoResponse getCourierMetaInfoBetweenStartDateAndEndDate(long courierId, LocalDate startDate, LocalDate endDate);

    OrderAssignResponse getCouriersAssigns(LocalDate date, Long courierId);
}
