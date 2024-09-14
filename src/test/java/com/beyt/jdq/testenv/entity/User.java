package com.beyt.jdq.testenv.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "test_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "age")
    private Integer age;

    @Column(name = "birthdate")
    private Instant birthdate;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private Type type;

    public enum Status {
        ACTIVE, PASSIVE
    }

    public enum Type {
        ADMIN, USER
    }
}
