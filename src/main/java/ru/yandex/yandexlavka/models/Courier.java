package ru.yandex.yandexlavka.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "courier")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CourierType type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "courier")
    private List<WorkingHour> workingHour;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "courier")
    private List<CourierRegion> region;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "courier")
    private List<Order> orders;

    @Override
    public String toString() {
        return "Courier{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Courier courier = (Courier) o;
        return Objects.equals(id, courier.id) && type == courier.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
