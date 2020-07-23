package ru.logisticplatform.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@MappedSuperclass
public abstract class AbstractPersistableEntity<ID> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ID id;

    @Version
    Long version;
}
