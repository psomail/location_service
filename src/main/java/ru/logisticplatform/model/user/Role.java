package ru.logisticplatform.model.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

/**
 * Simple domain object that represents application user's role - ADMIN, USER, etc.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@Entity
@Table(name = "roles")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity {

    @Column(name = "name")
    String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    List<User> users;

    @Override
    public String toString() {
        return "Role{" +
                "id: " + super.getId() + ", " +
                "name: " + name + "}";
    }
}