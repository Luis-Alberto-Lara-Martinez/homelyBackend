package org.educa.homelyBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "property_statuses", uniqueConstraints = {@UniqueConstraint(name = "property_statuses_name_key",
        columnNames = {"name"})})
public class PropertyStatuses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
}