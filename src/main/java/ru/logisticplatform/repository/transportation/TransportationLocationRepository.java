package ru.logisticplatform.repository.transportation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.transportation.TransportationLocation;

public interface TransportationLocationRepository extends JpaRepository<TransportationLocation, Long> {
}
