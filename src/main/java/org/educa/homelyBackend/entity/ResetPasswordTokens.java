package org.educa.homelyBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@DynamicInsert
@Table(name = "reset_password_tokens", indexes = {
        @Index(name = "idx_reset_password_tokens_id_user",
                columnList = "id_user"),
        @Index(name = "idx_reset_password_tokens_hash_token",
                columnList = "hash_token"),
        @Index(name = "idx_reset_password_tokens_expiration_used",
                columnList = "expiration, used")})
public class ResetPasswordTokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_user", nullable = false)
    private Users idUser;

    @Column(name = "hash_token", nullable = false)
    private String hashToken;

    @Column(name = "expiration", nullable = false)
    private Instant expiration;

    @ColumnDefault("false")
    @Column(name = "used", nullable = false)
    private Boolean used;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}