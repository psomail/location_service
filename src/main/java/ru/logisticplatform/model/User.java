package ru.logisticplatform.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Simple JavaBean domain object that represents an User
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@Entity
@Table(name = "users")
@Data
public class User {

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
}
