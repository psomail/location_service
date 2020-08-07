package ru.logisticplatform.model.transportation;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.BaseEntity;

import javax.persistence.*;


@Entity
@Table(name = "transportationlocations")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportationLocation extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transportation_id")
    Transportation transportation;

    @Column(name = "lat")
    Double lat;

    @Column(name = "lon")
    Double lon;
}
