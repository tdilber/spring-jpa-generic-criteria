package com.beyt.jdq.testenv.entity.authorization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "my_authorization")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Authorization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "menu_url")
    private String menuUrl;

    @Column(name = "menu_icon")
    private String menuIcon;

    public Authorization(Long id, String name, String menuUrl, String menuIcon) {
        this.id = id;
        this.name = name;
        this.menuUrl = menuUrl;
        this.menuIcon = menuIcon;
    }

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "authorization", fetch = FetchType.LAZY)
    Set<RoleAuthorization> roleAuthorizations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authorization that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(menuUrl, that.menuUrl) && Objects.equals(menuIcon, that.menuIcon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, menuUrl, menuIcon);
    }
}
