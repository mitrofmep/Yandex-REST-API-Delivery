package ru.yandex.yandexlavka.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "assignment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourierAssign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    private LocalDate date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "assign_id")
    private List<GroupedOrders> orders;
}