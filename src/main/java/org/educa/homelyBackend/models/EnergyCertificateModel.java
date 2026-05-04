package org.educa.homelyBackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "energy_certificates")
public class EnergyCertificateModel {

    @Id
    @Column(name = "property_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "property_id", nullable = false)
    private PropertyModel property;

    @Column(name = "has_certificate", nullable = false)
    private Boolean hasCertificate;

    @Column(name = "consumption_scale", length = 1)
    private String consumptionScale;

    @Column(name = "consumption_value")
    private Integer consumptionValue;

    @Column(name = "emissions_scale", length = 1)
    private String emissionsScale;

    @Column(name = "emissions_value")
    private Integer emissionsValue;
}
