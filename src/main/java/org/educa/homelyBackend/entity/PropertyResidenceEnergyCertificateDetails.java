package org.educa.homelyBackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "property_residence_energy_certificate_details", indexes = {@Index(name = "uq_property_residence_energy_certificate_id_property",
        columnList = "id_property",
        unique = true)})
public class PropertyResidenceEnergyCertificateDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_property", nullable = false)
    private Properties idProperty;

    @Column(name = "has_energy_certificate", nullable = false)
    private Boolean hasEnergyCertificate;

    @Column(name = "consumption_efficiency_scale", length = Integer.MAX_VALUE)
    private String consumptionEfficiencyScale;

    @Column(name = "consumption_efficiency_value")
    private Integer consumptionEfficiencyValue;

    @Column(name = "emissions_efficiency_scale", length = Integer.MAX_VALUE)
    private String emissionsEfficiencyScale;

    @Column(name = "emissions_efficiency_value")
    private Integer emissionsEfficiencyValue;

    @OneToOne(mappedBy = "idEnergyCertificate")
    private PropertyResidenceDetails propertyResidenceDetail;
}