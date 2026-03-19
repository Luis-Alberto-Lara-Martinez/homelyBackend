package org.educa.homelyBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "users_auth", indexes = {@Index(name = "idx_users_auth_id_user",
        columnList = "id_user")}, uniqueConstraints = {@UniqueConstraint(name = "uq_user_provider",
        columnNames = {
                "id_user",
                "provider"})})
public class UsersAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_user", nullable = false)
    private Users idUser;

    @Column(name = "provider", nullable = false, length = 50)
    private String provider;

    @Column(name = "hash_password")
    private String hashPassword;
}