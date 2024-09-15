package com.beyt.jdq.testenv.entity.authorization;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "admin_user")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "admin_user_role",
      joinColumns = @JoinColumn(name = "admin_user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
    List<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminUser adminUser)) return false;
        return Objects.equals(id, adminUser.id) && Objects.equals(username, adminUser.username) && Objects.equals(password, adminUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
