package ru.yandex.yandexlavka.repositories;


import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.models.Courier;

@Repository
public interface CourierRepository extends ListCrudRepository<Courier, Long> {

}
