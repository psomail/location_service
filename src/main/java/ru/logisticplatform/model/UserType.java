package ru.logisticplatform.model;

import lombok.Data;
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
public class UserType extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "userTypes", fetch = FetchType.LAZY)
    private List<User> users;

    @Override
    public String toString() {
        return "UserType{" +
                "id: " + super.getId() + ", " +
                "name: " + name + "}";
    }
}
