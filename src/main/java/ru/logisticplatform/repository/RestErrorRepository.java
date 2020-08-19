package ru.logisticplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.RestError;

public interface RestErrorRepository extends JpaRepository<RestError, Long> {
}
