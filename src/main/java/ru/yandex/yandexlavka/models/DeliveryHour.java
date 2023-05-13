package ru.yandex.yandexlavka.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "delivery_hour")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start")
    private LocalTime start;

    @Column(name = "finish")
    private LocalTime finish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}
