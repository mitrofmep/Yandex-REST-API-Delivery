package ru.yandex.yandexlavka.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.dto.*;
import ru.yandex.yandexlavka.exceptions.CourierNotFoundException;
import ru.yandex.yandexlavka.models.Courier;
import ru.yandex.yandexlavka.models.CourierAssign;
import ru.yandex.yandexlavka.models.CourierType;
import ru.yandex.yandexlavka.models.Order;
import ru.yandex.yandexlavka.repositories.CourierAssignRepository;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.repositories.OrderRepository;
import ru.yandex.yandexlavka.util.CourierAssignToCouriersGroupOrdersMapper;
import ru.yandex.yandexlavka.util.CourierToCourierDtoMapper;
import ru.yandex.yandexlavka.util.CreateCourierDtoToCourierMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;
    private final CourierToCourierDtoMapper courierToCourierDtoMapper;
    private final CreateCourierDtoToCourierMapper createCourierDtoToCourierMapper;
    private final OrderRepository orderRepository;
    private final OrderServiceImpl orderService;
    private final CourierAssignRepository courierAssignRepository;
    private final CourierAssignToCouriersGroupOrdersMapper courierAssignToCouriersGroupOrdersMapper;

    @Override
    public CreateCourierResponse createCouriers(CreateCourierRequest createCourierRequest) {
        List<CourierDto> courierDtos = new ArrayList<>();

        for (CreateCourierDto createCourierDto : createCourierRequest.getCouriers()) {
            Courier courier = createCourierDtoToCourierMapper.mapToModel(createCourierDto);
            Courier savedCourier = courierRepository.save(courier);
            CourierDto courierDto = courierToCourierDtoMapper.mapToDto(savedCourier);
            courierDtos.add(courierDto);
        }

        CreateCourierResponse response = new CreateCourierResponse();

        response.setCouriers(courierDtos);

        return response;
    }

    @Override
    public GetCouriersResponse getCouriers(int limit, int offset) {
        List<CourierDto> courierDtoList = courierRepository.findAll().stream().skip(offset).limit(limit).map(courierToCourierDtoMapper::mapToDto).toList();

        GetCouriersResponse response = new GetCouriersResponse();

        response.setCouriers(courierDtoList);
        response.setLimit(limit);
        response.setOffset(offset);

        return response;
    }

    @Override
    public CourierDto getCourier(long courierId) {
        Optional<Courier> courier = courierRepository.findById(courierId);

        if (courier.isEmpty()) throw new CourierNotFoundException();

        return courierToCourierDtoMapper.mapToDto(courier.get());
    }

    @Override
    public GetCourierMetaInfoResponse getCourierMetaInfoBetweenStartDateAndEndDate(long courierId, LocalDate startDate, LocalDate endDate) {

        Courier courier = courierRepository.findById(courierId).orElseThrow(CourierNotFoundException::new);
        CourierDto courierDto = courierToCourierDtoMapper.mapToDto(courier);

        GetCourierMetaInfoResponse response = new GetCourierMetaInfoResponse();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        List<Order> orders = orderRepository.findAllByCourierIdAndCompleteTimeBetween(courierId, startDateTime, endDateTime);

        response.setCourierId(courierDto.getId());
        response.setCourierType(courierDto.getCourierType());
        response.setRegions(courierDto.getRegions());
        response.setWorkingHours(courierDto.getWorkingHours());

        if (orders.isEmpty()) {
            response.setRating(0);
            response.setEarnings(0);
            return response;
        }

        int earnings = orders.stream()
                .mapToInt(Order::getCost)
                .map(cost -> cost * getMultiplierForEarnings(courier.getType())).sum();

        int rating = (int) (orders.size() / ChronoUnit.HOURS.between(startDate.atStartOfDay(), endDate.atStartOfDay()) * getMultiplierForRating(courier.getType()));
        response.setRating(rating);
        response.setEarnings(earnings);

        return response;
    }

    private static int getMultiplierForEarnings(CourierType courierType) {
        switch (courierType) {
            case AUTO -> {
                return 4;
            }
            case BIKE -> {
                return 3;
            }
            case FOOT -> {
                return 2;
            }
            default -> throw new IllegalArgumentException("Unknown courierType" + courierType);
        }
    }

    private static int getMultiplierForRating(CourierType courierType) {
        switch (courierType) {
            case AUTO -> {
                return 1;
            }
            case BIKE -> {
                return 2;
            }
            case FOOT -> {
                return 3;
            }
            default -> throw new IllegalArgumentException("Unknown courierType" + courierType);
        }
    }

    public OrderAssignResponse getCouriersAssigns(LocalDate date, Long courierId) {
        List<CourierAssign> courierAssignList = null;
        if (courierId != null) {
            courierAssignList = courierAssignRepository.findAllByDateAndCourierId(date, courierId);
        } else {
            courierAssignList = courierAssignRepository.findAllByDate(date);
        }

        OrderAssignResponse orderAssignResponse = new OrderAssignResponse();

        orderAssignResponse.setDate(date.toString());

        orderAssignResponse.setCouriers(courierAssignList.stream().map(courierAssignToCouriersGroupOrdersMapper::mapToDto).toList());

        return orderAssignResponse;
    }
}
