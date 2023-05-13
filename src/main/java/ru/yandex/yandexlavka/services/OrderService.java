package ru.yandex.yandexlavka.services;

import ru.yandex.yandexlavka.dto.CompleteOrderRequestDto;
import ru.yandex.yandexlavka.dto.CreateOrderRequest;
import ru.yandex.yandexlavka.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> createOrders(CreateOrderRequest createOrderRequest);

    List<OrderDto> getOrders(int limit, int offset);

    OrderDto getOrder(long orderId);

    List<OrderDto> completeOrders(CompleteOrderRequestDto completeOrderRequestDto);
}
