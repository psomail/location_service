package ru.logisticplatform.model.order;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.AbstractAuditableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "transport_type")
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

}
