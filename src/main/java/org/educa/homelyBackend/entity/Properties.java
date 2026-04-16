package org.educa.homelyBackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "properties", indexes = {
        @Index(name = "idx_properties_id_user",
                columnList = "id_user"),
        @Index(name = "idx_properties_id_type",
                columnList = "id_type"),
        @Index(name = "idx_properties_status_transaction_price",
                columnList = "id_status, id_transaction, price"),
        @Index(name = "idx_properties_id_status",
                columnList = "id_status"),
        @Index(name = "idx_properties_id_transaction",
                columnList = "id_transaction"),
        @Index(name = "idx_properties_id_address",
                columnList = "id_address"),
        @Index(name = "idx_properties_created_at",
                columnList = "created_at"),
        @Index(name = "idx_properties_updated_by",
                columnList = "updated_by")})
public class Properties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_user", nullable = false)
    private Users idUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_type", nullable = false)
    private PropertyTypes idType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_status", nullable = false)
    private PropertyStatuses idStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_transaction", nullable = false)
    private PropertyTransactions idTransaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_address", nullable = false)
    private PropertyAddresses idAddress;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "surface", nullable = false)
    private Integer surface;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Users updatedBy;

    @OneToMany(mappedBy = "idProperty")
    private Set<Favorites> favorites = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idProperty")
    private Set<Messages> messages = new LinkedHashSet<>();

    @ManyToMany
    private Set<PropertyExtras> propertyExtras = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idProperty")
    private Set<PropertyImages> propertyImages = new LinkedHashSet<>();

    @OneToOne(mappedBy = "idProperty")
    private PropertyResidenceDetails propertyResidenceDetail;

    @OneToOne(mappedBy = "idProperty")
    private PropertyResidenceEnergyCertificateDetails propertyResidenceEnergyCertificateDetail;
}