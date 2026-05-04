package org.educa.homelyBackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "property_extras",
        uniqueConstraints = {
                @UniqueConstraint(name = "property_extras_name_key", columnNames = {"name"})
        }
)
public class PropertyExtraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "properties_property_extras",
            joinColumns = @JoinColumn(name = "extra_id"),
            inverseJoinColumns = @JoinColumn(name = "property_id")
    )
    private Set<PropertyModel> properties = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "property_extras_property_types",
            joinColumns = @JoinColumn(name = "extra_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private Set<PropertyTypeModel> propertyTypes = new LinkedHashSet<>();
}
