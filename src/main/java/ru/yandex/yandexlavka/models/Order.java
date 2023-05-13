package ru.yandex.yandexlavka.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "weight")
    private float weight;

    @Column(name = "region")
    private int region;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private List<DeliveryHour> deliveryHours;

    @Column(name = "cost")
    private int cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", weight=" + weight +
                ", region=" + region +
                ", cost=" + cost +
                ", status=" + status +
                ", completeTime=" + completeTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Float.compare(order.weight, weight) == 0 && region == order.region && cost == order.cost && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weight, region, cost);
    }
}
