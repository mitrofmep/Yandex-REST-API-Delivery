package ru.yandex.yandexlavka.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.models.Order;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends ListCrudRepository<Order, Long> {
    @Query("SELECT o FROM Order o left join fetch o.courier" +
            " c where c.id = :courierId and o.status = 'FINISHED' and o.completeTime >= :startDate and o.completeTime < :endDate")
    List<Order> findAllByCourierIdAndCompleteTimeBetween(long courierId, LocalDateTime startDate, LocalDateTime endDate);
}
