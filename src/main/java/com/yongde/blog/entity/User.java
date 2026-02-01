package com.yongde.blog.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue()
    private long id;

    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(updatable = false)
    private Instant created;

    private Instant updated;

    public User() {}

    private User(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;

    }
    private User(Builder builder) {

    }

    public static Builder bulder() { return new Builder(); }


}
