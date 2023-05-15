package ru.yandex.yandexlavka.util;

import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.dto.OrderDto;
import ru.yandex.yandexlavka.models.DeliveryHour;
import ru.yandex.yandexlavka.models.Order;

@Component
public class OrderToOrderDtoMapper implements ModelToDtoMapper<Order, OrderDto> {
    @Override
    public OrderDto mapToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setWeight(order.getWeight());
        orderDto.setCost(order.getCost());
        orderDto.setRegion(order.getRegion());
        orderDto.setDeliveryHours(order.getDeliveryHours().stream().map(this::mapDeliveryHourToString).toList());
        if (order.getCompleteTime() != null) {
            orderDto.setCompletedTime(order.getCompleteTime().toString());
        }
        return orderDto;
    }

    private String mapDeliveryHourToString(DeliveryHour deliveryHour) {
        return String.format("%s-%s", deliveryHour.getStart(), deliveryHour.getFinish());
    }
}
