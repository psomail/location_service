package ru.logisticplatform.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.BaseEntity;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.transportation.Transportation;

import javax.persistence.*;
import java.util.List;

/**
 * Simple JavaBean domain object that represents an User
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@Entity
@Table(name = "users")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(name = "username")
    String username;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "email")
    String email;

    @Column(name = "phone")
    String phone;

    @Column(name = "password")
    String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    List<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_usertypes",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "usertype_id", referencedColumnName = "id")})
    List<UserType> userTypes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    UserStatus userStatus;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    //@JsonIgnore
    List<Goods> goods;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Order> orders;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Transportation> transportations;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", userTypes=" + userTypes +
                ", userStatus=" + userStatus +
                '}';
    }
}
