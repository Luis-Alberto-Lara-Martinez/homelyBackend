package org.educa.homelyBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_roles", indexes = {@Index(name = "idx_user_roles_name",
        columnList = "name")}, uniqueConstraints = {@UniqueConstraint(name = "user_roles_name_key",
        columnNames = {"name"})})
public class UserRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "idRole")
    private Set<Users> users = new LinkedHashSet<>();
}