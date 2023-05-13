package ru.yandex.yandexlavka.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "working_hour")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkingHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start")
    private LocalTime start;

    @Column(name = "finish")
    private LocalTime finish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @Override
    public String toString() {
        return "WorkingHour{" +
                "id=" + id +
                ", start=" + start +
                ", finish=" + finish +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkingHour that = (WorkingHour) o;
        return Objects.equals(id, that.id) && Objects.equals(start, that.start) && Objects.equals(finish, that.finish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, finish);
    }
}
