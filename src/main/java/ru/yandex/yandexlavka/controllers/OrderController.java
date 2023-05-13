package ru.yandex.yandexlavka.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.dto.CompleteOrderRequestDto;
import ru.yandex.yandexlavka.dto.CreateOrderRequest;
import ru.yandex.yandexlavka.dto.OrderDto;
import ru.yandex.yandexlavka.services.OrderServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Validated
public class OrderController {
    private final OrderServiceImpl orderService;

    @PostMapping
    @RateLimiter(name = "createOrders")
    public List<OrderDto> createOrders(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        return orderService.createOrders(createOrderRequest);
    }

    @GetMapping
    @RateLimiter(name = "getOrders")
    public List<OrderDto> getOrders(@RequestParam(required = false, defaultValue = "1") @PositiveOrZero int limit,
                                    @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int offset) {
        return orderService.getOrders(limit, offset);
    }

    @GetMapping("/{order_id}")
    @RateLimiter(name = "getOrderById")
    public OrderDto getOrderById(@PathVariable(name = "order_id") long orderId) {
        return orderService.getOrder(orderId);
    }

    @PostMapping("/complete")
    @RateLimiter(name = "completeOrders")
    public List<OrderDto> completeOrders(@RequestBody @Valid CompleteOrderRequestDto completeOrderRequestDto) {
        return orderService.completeOrders(completeOrderRequestDto);
    }


}
