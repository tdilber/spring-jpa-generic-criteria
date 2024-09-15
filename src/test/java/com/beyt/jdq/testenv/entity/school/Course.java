package com.beyt.jdq.testenv.entity.school;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@ToString
@Getter
@Setter
@Entity
@Table(name = "course")
@NoArgsConstructor
@FieldNameConstants
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Timestamp startDate;

    private Integer maxStudentCount;

    private Boolean active;

    private String description;

    public Course(Long id, String name, Timestamp startDate, Integer maxStudentCount, Boolean active, String description) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.maxStudentCount = maxStudentCount;
        this.active = active;
        this.description = description;
    }

    public static Course of(String name, String description) {
        return new Course(null, name, null, null, null, description);
    }

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Student> students;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return Objects.equals(id, course.id) && Objects.equals(name, course.name) && Objects.equals(startDate, course.startDate) && Objects.equals(maxStudentCount, course.maxStudentCount) && Objects.equals(active, course.active) && Objects.equals(description, course.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate, maxStudentCount, active, description);
    }
}
