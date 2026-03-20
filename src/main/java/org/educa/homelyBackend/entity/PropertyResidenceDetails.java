package org.educa.homelyBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "property_residence_details", indexes = {
        @Index(name = "uq_property_residence_details_id_property",
                columnList = "id_property",
                unique = true),
        @Index(name = "uq_property_residence_details_id_energy_certificate",
                columnList = "id_energy_certificate",
                unique = true)})
public class PropertyResidenceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_property", nullable = false)
    private Properties idProperty;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_energy_certificate", nullable = false)
    private PropertyResidenceEnergyCertificateDetails idEnergyCertificate;

    @Column(name = "bedrooms", nullable = false)
    private Integer bedrooms;

    @Column(name = "bathrooms", nullable = false)
    private Integer bathrooms;

    @Column(name = "conservation", nullable = false, length = 50)
    private String conservation;

    @Column(name = "orientation", length = 2)
    private String orientation;
}