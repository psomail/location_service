package ru.logisticplatform.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.logisticplatform.model.user.UserStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Base SuperClass with common fields
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@MappedSuperclass
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    @Column(name = "created")
    @CreationTimestamp
    Date created;

    @LastModifiedDate
    @Column(name = "updated")
    @UpdateTimestamp
    Date updated;
}