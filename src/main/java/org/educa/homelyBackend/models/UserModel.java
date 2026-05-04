package org.educa.homelyBackend.models;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_users_role_id", columnList = "role_id"),
                @Index(name = "idx_users_status_id", columnList = "status_id"),
                @Index(name = "idx_users_created_by", columnList = "created_by"),
                @Index(name = "idx_users_updated_by", columnList = "updated_by")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "users_email_key", columnNames = {"email"})
        }
)
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private UserRoleModel role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private UserStatusModel status;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "hashed_password")
    private String hashedPassword;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserModel createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private UserModel updatedBy;

    @ManyToMany(mappedBy = "users")
    private Set<ConversationModel> conversations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<FavouriteModel> favourites = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<PropertyModel> propertiesCreatedBy = new LinkedHashSet<>();

    @OneToMany(mappedBy = "updatedBy")
    private Set<PropertyModel> propertiesUpdatedBy = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<ResetTokenModel> resetTokens = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<UserModel> usersCreatedBy = new LinkedHashSet<>();

    @OneToMany(mappedBy = "updatedBy")
    private Set<UserModel> usersUpdatedBy = new LinkedHashSet<>();
}
