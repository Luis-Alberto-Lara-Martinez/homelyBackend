package org.educa.homelyBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "property_types", uniqueConstraints = {@UniqueConstraint(name = "property_types_name_key",
        columnNames = {"name"})})
public class PropertyTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "idType")
    private Set<Properties> properties = new LinkedHashSet<>();

    @ManyToMany
    private Set<PropertyExtras> propertyExtras = new LinkedHashSet<>();
}