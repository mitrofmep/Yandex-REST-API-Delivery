package ru.yandex.yandexlavka.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.dto.*;
import ru.yandex.yandexlavka.exceptions.InvalidCompleteOrderRequestDtoException;
import ru.yandex.yandexlavka.exceptions.OrderNotFoundException;
import ru.yandex.yandexlavka.models.Order;
import ru.yandex.yandexlavka.models.OrderStatus;
import ru.yandex.yandexlavka.repositories.OrderRepository;
import ru.yandex.yandexlavka.util.CreateOrderDtoToOrderMapper;
import ru.yandex.yandexlavka.util.OrderToOrderDtoMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CreateOrderDtoToOrderMapper createOrderDtoToOrderMapper;
    private final OrderToOrderDtoMapper orderToOrderDtoMapper;

    @Override
    public List<OrderDto> createOrders(CreateOrderRequest createOrderRequest) {
        List<Order> orders = new ArrayList<>();

        List<CreateOrderDto> createOrderDtos = createOrderRequest.getOrders();

        for (CreateOrderDto createOrderDto : createOrderDtos) {
            Order order = createOrderDtoToOrderMapper.mapToModel(createOrderDto);
            orders.add(order);
        }

        List<Order> savedOrders = orderRepository.saveAll(orders);

        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order savedOrder : savedOrders) {
            OrderDto orderDto = orderToOrderDtoMapper.mapToDto(savedOrder);
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    @Override
    public List<OrderDto> getOrders(int limit, int offset) {
        return orderRepository.findAll().stream().skip(offset).limit(limit).map(orderToOrderDtoMapper::mapToDto).toList();
    }

    @Override
    public OrderDto getOrder(long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isEmpty()) throw new OrderNotFoundException();

        return orderToOrderDtoMapper.mapToDto(order.get());
    }

    @Override
    public List<OrderDto> completeOrders(CompleteOrderRequestDto completeOrderRequestDto) {
        List<Order> completedOrders = new ArrayList<>();
        List<CompleteOrder> completeOrders = completeOrderRequestDto.getCompleteOrders();

        for (CompleteOrder completeOrder : completeOrders) {
            Order orderInRepo = orderRepository
                    .findById(completeOrder.getOrderId())
                    .orElseThrow(InvalidCompleteOrderRequestDtoException::new);
            if (checkOrderIsValid(orderInRepo, completeOrder)) {
                throw new InvalidCompleteOrderRequestDtoException();
            }
            orderInRepo.setCompleteTime(LocalDateTime.parse(completeOrder.getCompleteTime(), DateTimeFormatter.ISO_DATE_TIME));
            orderInRepo.setStatus(OrderStatus.FINISHED);
            completedOrders.add(orderInRepo);
        }

        orderRepository.saveAll(completedOrders);

        List<OrderDto> completedOrderDtos = new ArrayList<>();
        for (Order completedOrder : completedOrders) {
            OrderDto orderDto = orderToOrderDtoMapper.mapToDto(completedOrder);
            completedOrderDtos.add(orderDto);
        }

        return completedOrderDtos;
    }

    private boolean checkOrderIsValid(Order order, CompleteOrder completeOrder) {
        if (order.getStatus() != OrderStatus.ASSIGNED || order.getCourier().getId() != completeOrder.getCourierId()) {
            return true;
        } else {
            return false;
        }
    }

}
