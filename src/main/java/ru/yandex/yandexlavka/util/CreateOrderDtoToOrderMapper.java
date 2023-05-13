package ru.yandex.yandexlavka.util;

import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.dto.CreateOrderDto;
import ru.yandex.yandexlavka.models.DeliveryHour;
import ru.yandex.yandexlavka.models.Order;
import ru.yandex.yandexlavka.models.OrderStatus;

import java.time.LocalTime;

@Component
public class CreateOrderDtoToOrderMapper implements DtoToModelMapper<CreateOrderDto, Order> {
    @Override
    public Order mapToModel(CreateOrderDto dto) {
        Order order = new Order();

        order.setCost(dto.getCost());
        order.setRegion(dto.getRegion());

        order.setDeliveryHours(
                dto.getDeliveryHours().stream().map(hours -> getDeliveryHour(order, hours)).toList());

        order.setStatus(OrderStatus.NEW);
        order.setWeight(dto.getWeight());

        return order;
    }

    private DeliveryHour getDeliveryHour(Order order, String hours) {

        DeliveryHour deliveryHour = new DeliveryHour();

        deliveryHour.setOrder(order);

        String[] hoursArray = hours.split("-");

        deliveryHour.setStart(LocalTime.parse(hoursArray[0]));
        deliveryHour.setFinish(LocalTime.parse(hoursArray[1]));

        return deliveryHour;
    }
}
