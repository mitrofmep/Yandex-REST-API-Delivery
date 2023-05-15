package ru.yandex.yandexlavka.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.dto.CouriersGroupOrders;
import ru.yandex.yandexlavka.models.CourierAssign;

@Component
@RequiredArgsConstructor
public class CourierAssignToCouriersGroupOrdersMapper implements ModelToDtoMapper<CourierAssign, CouriersGroupOrders> {

    @Autowired
    private final GroupedOrdersToGroupOrdersMapper groupedOrdersToGroupOrdersMapper;

    @Override
    public CouriersGroupOrders mapToDto(CourierAssign courierAssign) {
        CouriersGroupOrders couriersGroupOrders = new CouriersGroupOrders();

        couriersGroupOrders.setCourierId(courierAssign.getCourier().getId());

        couriersGroupOrders.setOrders(courierAssign.getOrders().stream().map(groupedOrdersToGroupOrdersMapper::mapToDto).toList());

        return couriersGroupOrders;
    }
}
