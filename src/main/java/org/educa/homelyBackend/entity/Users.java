package org.educa.homelyBackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "users", indexes = {
        @Index(name = "idx_users_id_role",
                columnList = "id_role"),
        @Index(name = "idx_users_id_status",
                columnList = "id_status"),
        @Index(name = "idx_users_email",
                columnList = "email"),
        @Index(name = "idx_users_created_by",
                columnList = "created_by"),
        @Index(name = "idx_users_updated_by",
                columnList = "updated_by")}, uniqueConstraints = {@UniqueConstraint(name = "users_email_key",
        columnNames = {"email"})})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_role", nullable = false)
    private UserRoles idRole;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_status", nullable = false)
    private UserStatuses idStatus;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "hash_password")
    private String hashPassword;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Users createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Users updatedBy;

    @OneToMany(mappedBy = "idUser")
    private Set<Favorites> favorites = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idSender")
    private Set<Messages> messagesSender = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idReceiver")
    private Set<Messages> messagesReceiver = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUser")
    private Set<Properties> propertiesIdUser = new LinkedHashSet<>();

    @OneToMany(mappedBy = "updatedBy")
    private Set<Properties> propertiesUpdateBy = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUser")
    private Set<ResetPasswordTokens> resetPasswordTokens = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<Users> usersCreatedBy = new LinkedHashSet<>();

    @OneToMany(mappedBy = "updatedBy")
    private Set<Users> usersUpdatedBy = new LinkedHashSet<>();
}