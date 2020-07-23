package ru.logisticplatform.model.order;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.AbstractAuditableEntity;
import ru.logisticplatform.model.user.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "services")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Service extends AbstractAuditableEntity<User, Long> implements Serializable {

    @Column(name = "name")
    String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transport_type_id")
    TransportType transportType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    ServiceStatus serviceStatus = ServiceStatus.CREATED;

}
