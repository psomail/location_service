package ru.logisticplatform.model.order;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.AbstractAuditableEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "transport_types")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportType extends AbstractAuditableEntity<Service, Long> implements Serializable {

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "lenght")
    Double lenght;

    @Column(name = "width")
    Double width;

    @Column(name = "height")
    Double height;

    @Column(name = "volume")
    Double volume;

    @Column(name = "carrying")
    Double carrying;

    @OneToMany(mappedBy = "transportType", fetch = FetchType.LAZY)
    List<Service> services;

}
