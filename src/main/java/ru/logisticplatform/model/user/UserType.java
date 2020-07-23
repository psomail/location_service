package ru.logisticplatform.model.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.user.BaseEntity;
import ru.logisticplatform.model.user.User;

import javax.persistence.*;
import java.util.List;

/**
 * Simple domain object that represents application user's type - CONTRACTOR, CUSTOMER, etc.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@Entity
@Table(name = "usertypes")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserType extends BaseEntity {

    @Column(name = "name")
    String name;

    @ManyToMany(mappedBy = "userTypes", fetch = FetchType.LAZY)
    List<User> users;

    @Override
    public String toString() {
        return "UserType{" +
                "id: " + super.getId() + ", " +
                "name: " + name + "}";
    }
}
