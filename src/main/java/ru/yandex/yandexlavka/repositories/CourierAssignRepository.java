package ru.yandex.yandexlavka.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.models.CourierAssign;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourierAssignRepository extends ListCrudRepository<CourierAssign, Long> {

    List<CourierAssign> findAllByDateAndCourierId(LocalDate localdate, long courier_id);

    List<CourierAssign> findAllByDate(LocalDate localDate);
}
