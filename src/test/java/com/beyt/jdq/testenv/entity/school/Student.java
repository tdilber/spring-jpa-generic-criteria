package com.beyt.jdq.testenv.entity.school;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Fetch;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@ToString
@Getter
@Setter
@Entity
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Student(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="department_id", nullable=true)
    private Department department;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    @JoinTable(
      name = "StudentCourse",
      joinColumns = @JoinColumn(name = "student_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id"))
    List<Course> courses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return Objects.equals(id, student.id) && Objects.equals(name, student.name);
    }
    public static Student of(String name) {
        Student student = new Student();
        student.setName(name);
        return student;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
