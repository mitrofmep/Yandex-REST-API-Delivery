package ru.yandex.yandexlavka.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.models.Courier;
import ru.yandex.yandexlavka.models.CourierType;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface CourierRepository extends ListCrudRepository<Courier, Long> {
    @Query("SELECT c FROM Courier c " +
            " left JOIN c.region r" +
            " left join c.workingHour wh " +
            " where r.region = :region and c.type in :types" +
            " and wh.start < :deliveryFinish and wh.finish > :deliveryStart ")
    List<Courier> findPotentialCouriers(@Param("region") int region,
                                        @Param("types") List<CourierType> types,
                                        @Param("deliveryStart") LocalTime deliveryStart,
                                        @Param("deliveryFinish") LocalTime deliveryFinish);
}
