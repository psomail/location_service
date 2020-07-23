package ru.logisticplatform.model.order;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.AbstractAuditableEntity;
import ru.logisticplatform.model.user.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "service")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Service extends AbstractAuditableEntity<User, Long> implements Serializable {

    @Column(name = "name")
    String name;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    ServiceStatus serviceStatus = ServiceStatus.CREATED;

}
