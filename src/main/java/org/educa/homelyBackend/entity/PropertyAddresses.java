package org.educa.homelyBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "property_addresses", indexes = {
        @Index(name = "idx_property_addresses_city_postal_code",
                columnList = "city, postal_code"),
        @Index(name = "idx_property_addresses_province_country",
                columnList = "province, country"),
        @Index(name = "idx_property_addresses_latitude_longitude",
                columnList = "latitude, longitude")})
public class PropertyAddresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "street_number", length = 10)
    private String streetNumber;

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

    @OneToMany(mappedBy = "idAddress")
    private Set<Properties> properties = new LinkedHashSet<>();
}