package org.educa.homelyBackend.models;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "property_addresses",
        indexes = {
                @Index(name = "uq_property_addresses_property_id", columnList = "property_id", unique = true),
                @Index(name = "idx_property_addresses_city", columnList = "city"),
                @Index(name = "idx_property_addresses_latitude_longitude", columnList = "latitude, longitude")
        }
)
public class PropertyAddressModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "property_id", nullable = false)
    private PropertyModel property;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number", length = 10)
    private String number;

    @Column(name = "floor", length = 10)
    private String floor;

    @Column(name = "door", length = 10)
    private String door;

    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;
}
