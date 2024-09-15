package com.beyt.jdq.testenv.entity.authorization;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "role")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    public Role(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    Set<RoleAuthorization> roleAuthorizations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return Objects.equals(id, role.id) && Objects.equals(name, role.name) && Objects.equals(description, role.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
