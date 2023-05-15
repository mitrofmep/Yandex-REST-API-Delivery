package ru.yandex.yandexlavka.util;

import ru.yandex.yandexlavka.dto.CouriersGroupOrders;
import ru.yandex.yandexlavka.dto.GroupOrders;
import ru.yandex.yandexlavka.dto.OrderAssignResponse;
import ru.yandex.yandexlavka.dto.OrderDto;
import ru.yandex.yandexlavka.models.Courier;
import ru.yandex.yandexlavka.services.OrderServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderAssignResponseGenerator {

    private final OrderServiceImpl orderService;

    public OrderAssignResponseGenerator(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    public OrderAssignResponse getResponse(List<Courier> couriers, LocalDate date) {

        OrderAssignResponse orderAssignResponse = new OrderAssignResponse();

        List<CouriersGroupOrders> couriersGroupOrders = new ArrayList<>();

        for (Courier courier :
                couriers) {
            CouriersGroupOrders couriersGroupOrdersElement = new CouriersGroupOrders();
            couriersGroupOrdersElement.setCourierId(courier.getId());
            List<GroupOrders> groupOrders = new ArrayList<>();
            List<OrderDto> ordersByCourier = orderService.getOrdersByCourier(courier.getId());
            GroupOrders groupOrder = new GroupOrders();
            groupOrder.setOrders(ordersByCourier);
            Random random = new Random();
            groupOrder.setId(Math.abs(random.nextLong() % 10000));
            groupOrders.add(groupOrder);
            couriersGroupOrdersElement.setOrders(groupOrders);
            couriersGroupOrders.add(couriersGroupOrdersElement);
        }

        orderAssignResponse.setDate(date.toString());
        orderAssignResponse.setCouriers(couriersGroupOrders);

        return orderAssignResponse;
    }

}
