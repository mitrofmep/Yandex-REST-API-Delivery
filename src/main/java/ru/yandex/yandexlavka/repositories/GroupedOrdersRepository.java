package ru.yandex.yandexlavka.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.models.GroupedOrders;

@Repository
public interface GroupedOrdersRepository extends ListCrudRepository<GroupedOrders, Long> {
}
