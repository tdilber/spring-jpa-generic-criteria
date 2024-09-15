package com.beyt.jdq.testenv.entity.school;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Objects;
import java.util.Set;

@ToString
@Getter
@Setter
@Entity
@Table(name = "department")
@NoArgsConstructor
@FieldNameConstants
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Department(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy="department", fetch = FetchType.EAGER)
    private Set<Student> students;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
