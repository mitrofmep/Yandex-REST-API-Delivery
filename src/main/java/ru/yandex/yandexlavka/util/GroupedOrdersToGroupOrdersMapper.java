package ru.yandex.yandexlavka.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.dto.GroupOrders;
import ru.yandex.yandexlavka.models.GroupedOrders;

@Component
@RequiredArgsConstructor
public class GroupedOrdersToGroupOrdersMapper implements ModelToDtoMapper<GroupedOrders, GroupOrders> {

    @Autowired
    private final OrderToOrderDtoMapper orderToOrderDtoMapper;

    @Override
    public GroupOrders mapToDto(GroupedOrders groupedOrders) {
        GroupOrders groupOrders = new GroupOrders();

        groupOrders.setId(groupedOrders.getId());

        groupOrders.setOrders(groupedOrders.getOrders().stream().map(orderToOrderDtoMapper::mapToDto).toList());

        return groupOrders;
    }
}