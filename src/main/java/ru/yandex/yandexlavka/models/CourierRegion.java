package ru.yandex.yandexlavka.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "courier_region")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourierRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "region")
    private int region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @Override
    public String toString() {
        return "CourierRegion{" +
                "id=" + id +
                ", region=" + region +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourierRegion that = (CourierRegion) o;
        return region == that.region && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, region);
    }
}
