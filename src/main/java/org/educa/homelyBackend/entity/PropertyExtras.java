package org.educa.homelyBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "property_extras", uniqueConstraints = {@UniqueConstraint(name = "property_extras_name_key",
        columnNames = {"name"})})
public class PropertyExtras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToMany(mappedBy = "propertyExtras")
    private Set<Properties> properties = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "propertyExtras")
    private Set<PropertyTypes> propertyTypes = new LinkedHashSet<>();
}