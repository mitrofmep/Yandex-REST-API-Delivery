package ru.yandex.yandexlavka.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "grouped_orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupedOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "grouped_orders_id")
    private List<Order> orders;
}