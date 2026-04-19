package org.educa.homelyBackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class PropertiesPropertyExtraModelId implements Serializable {

    @Column(name = "property_id", nullable = false)
    private Integer propertyId;

    @Column(name = "extra_id", nullable = false)
    private Integer extraId;
}
