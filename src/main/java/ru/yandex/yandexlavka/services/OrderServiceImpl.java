package ru.yandex.yandexlavka.services;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.dto.*;
import ru.yandex.yandexlavka.exceptions.InvalidCompleteOrderRequestDtoException;
import ru.yandex.yandexlavka.exceptions.OrderNotFoundException;
import ru.yandex.yandexlavka.models.Courier;
import ru.yandex.yandexlavka.models.CourierType;
import ru.yandex.yandexlavka.models.Order;
import ru.yandex.yandexlavka.models.OrderStatus;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.repositories.OrderRepository;
import ru.yandex.yandexlavka.util.CreateOrderDtoToOrderMapper;
import ru.yandex.yandexlavka.util.OrderAssignResponseGenerator;
import ru.yandex.yandexlavka.util.OrderToOrderDtoMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CreateOrderDtoToOrderMapper createOrderDtoToOrderMapper;
    private final OrderToOrderDtoMapper orderToOrderDtoMapper;
    private final CourierRepository courierRepository;

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
    public List<OrderDto> getOrdersByCourier(long courierId) {
        return orderRepository.findAllByCourierId(courierId).stream().map(orderToOrderDtoMapper::mapToDto).toList();
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

    @Override
    public List<OrderAssignResponse> assignOrders(LocalDate date) {
        List<OrderAssignResponse> orderAssignResponses = new ArrayList<>();

        // Все заказы со статусом NEW
        List<Order> allNewOrders = orderRepository.findAllByStatus(OrderStatus.NEW);


        // Map <заказ:список потенциальных курьеров> с учетом региона, веса, времени - done
        Map<Order, List<Courier>> ordersAndItsPotentialCouriers = new HashMap<>();
        for (Order order :
                allNewOrders) {
            List<CourierType> courierTypes = getCourierTypesFromWeight(order);
            List<Courier> potentialCouriers = courierRepository.findPotentialCouriers(order.getRegion(),
                    courierTypes, order.getDeliveryHours().get(0).getStart(),
                    order.getDeliveryHours().get(0).getFinish());
            ordersAndItsPotentialCouriers.put(order, potentialCouriers);
        }


        // Map<курьер, доступные ему заказы> - done
        HashMap<Courier, List<Order>> couriersAndItsOrders = new HashMap<Courier, List<Order>>();
        // уменьшить итерации
        Set<Courier> doneCouriers = new HashSet<>();
        for (Order order :
                ordersAndItsPotentialCouriers.keySet()) {
            List<Courier> couriersOfOrder = ordersAndItsPotentialCouriers.get(order);
            for (Courier courierInList :
                    couriersOfOrder) {
                if (!doneCouriers.contains(courierInList)) {
                    doneCouriers.add(courierInList);
                    List<Order> ordersForCourier = new ArrayList<>();

                    for (Order order2 :
                            ordersAndItsPotentialCouriers.keySet()) {
                        List<Courier> couriers2 = ordersAndItsPotentialCouriers.get(order2);
                        if (couriers2.contains(courierInList)) {
                            ordersForCourier.add(order2);
                        }
                    }
                    couriersAndItsOrders.put(courierInList, ordersForCourier);
                }
            }
        }

        Iterator<Map.Entry<Courier, List<Order>>> iterator = couriersAndItsOrders.entrySet().iterator();

        List<Courier> couriersToResponse = new ArrayList<>();


        while (iterator.hasNext()) {
            Map.Entry<Courier, List<Order>> entry = iterator.next();
            assignmentForOneCourier(entry.getKey(), entry.getValue());
            couriersToResponse.add(entry.getKey());
        }

        OrderAssignResponseGenerator generator = new OrderAssignResponseGenerator(this);
        OrderAssignResponse orderAssignResponse = generator.getResponse(couriersToResponse, date);

        orderAssignResponses.add(orderAssignResponse);

        return orderAssignResponses;
    }

    private void assignOrder(long order_id, Courier courier) {
        orderRepository.findById(order_id).ifPresent(o -> {
            o.setCourier(courier);
            o.setStatus(OrderStatus.ASSIGNED);
        });
    }

    private boolean checkOrderIsValid(Order order, CompleteOrder completeOrder) {
        if (order.getStatus() != OrderStatus.ASSIGNED || order.getCourier().getId() != completeOrder.getCourierId()) {
            return true;
        } else {
            return false;
        }
    }

    private List<CourierType> getCourierTypesFromWeight(Order order) {
        float weight = order.getWeight();
        List<CourierType> courierTypes = new ArrayList<>();

        if (weight <= 40) courierTypes.add(CourierType.AUTO);
        if (weight <= 20) courierTypes.add(CourierType.BIKE);
        if (weight <= 10) courierTypes.add(CourierType.FOOT);

        return courierTypes;
    }

    private void assignmentForOneCourier(Courier courier, List<Order> orders) {

        int maxOrders = 0;
        int maxRegions = 0;
        int capacity = 0;

        if (courier.getType().equals(CourierType.FOOT)) {
            maxOrders = 2;
            maxRegions = 1;
            capacity = 10;
        } else if (courier.getType().equals(CourierType.BIKE)) {
            maxOrders = 4;
            maxRegions = 2;
            capacity = 20;
        } else if (courier.getType().equals(CourierType.AUTO)) {
            maxOrders = 7;
            maxRegions = 3;
            capacity = 40;
        }


        // массив сумок, каждый заполняем макс емкостью
        float[] bag_remain = new float[10];
        Arrays.fill(bag_remain, capacity);

        Optional<Courier> foundCourier = courierRepository.findById(courier.getId());
        if (foundCourier.isPresent())
            courier = foundCourier.get();

        Hibernate.initialize(courier.getOrders());

        List<Order> currOrders = courier.getOrders();
        Set<Integer> regions = new HashSet<>();
        for (Order o :
                currOrders) {
            Hibernate.initialize(o.getRegion());
            regions.add(o.getRegion());
        }

        int currOrdersSize = currOrders.size();


        // промежуточное - минимум сумок для эффективной компоновки
        int res = 0;

        // пока не превышено допустимое количество заказов для курьера
        Order order = null;
        // перебор по каждому заказу
        for (int i = 0; i < orders.size(); i++) {
            if (currOrdersSize >= maxOrders || regions.size() >= maxRegions) return;
            Optional<Order> orderFound = orderRepository.findById(orders.get(i).getId());
            if (orderFound.isPresent()) order = orderFound.get();
            if (order.getStatus().equals(OrderStatus.FINISHED) || order.getStatus().equals(OrderStatus.ASSIGNED))
                continue;
            int j;
            // перебор по уже заполненным частично сумкам, если туда поместится еще
            for (j = 0; j < res; j++) {
                // если остаток грузоподъемности больше всего заказа
                if (bag_remain[j] >= order.getWeight()) {
                    // Вычитаем из оставшегося пустого места вес заказа.
                    bag_remain[j] = bag_remain[j] - order.getWeight();
                    // Назначаем заказ курьеру и прибавляем счетчик возможных заказов для него
                    currOrdersSize++;

                    order.setCourier(courier);
                    order.setStatus(OrderStatus.ASSIGNED);
                    orderRepository.save(order);
                    regions.add(order.getRegion());
                    break;
                }
            }
                /*
                 Если не смогли найти уже частично заполненный рюкзак для того, чтобы добавить туда наш заказ
                 На самой актуальной позиции рюкзаков (res сначала 0, а потом последний, на котором последний раз что-то делали)
                 вычитаем из capacity вес заказа, и обозначаем, что у нас теперь +1 рюкзак используется
                 */
            if (j == res) {
                // [20, 20, 20, 20] --> [3, 20, 20, 20]
                bag_remain[res] -= order.getWeight();
                // точно +1 рюкзак будет использован
                res++;
                // назначаем курьера, прибавляем счетчик
                order.setCourier(courier);
                order.setStatus(OrderStatus.ASSIGNED);
                orderRepository.save(order);
                regions.add(order.getRegion());
                currOrdersSize++;
            }
        }
    }

}
