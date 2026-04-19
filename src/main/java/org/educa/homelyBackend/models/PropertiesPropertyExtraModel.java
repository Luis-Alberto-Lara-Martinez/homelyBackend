package org.educa.homelyBackend.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "properties_property_extras",
        indexes = {
                @Index(name = "idx_properties_property_extras_id_property", columnList = "property_id"),
                @Index(name = "idx_properties_property_extras_id_extra", columnList = "extra_id"),
                @Index(name = "idx_properties_property_extras_id_type", columnList = "type_id")
        }
)
public class PropertiesPropertyExtraModel {

    @EmbeddedId
    private PropertiesPropertyExtraModelId id;

    @MapsId("propertyId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "property_id", nullable = false)
    private PropertyModel property;

    @MapsId("extraId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "extra_id", nullable = false)
    private PropertyExtraModel extra;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "type_id", nullable = false)
    private PropertyTypeModel type;
}
