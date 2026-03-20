package org.educa.homelyBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "messages", indexes = {
        @Index(name = "idx_messages_id_sender_created_at",
                columnList = "id_sender, created_at"),
        @Index(name = "idx_messages_id_receiver_is_read_created_at",
                columnList = "id_receiver, is_read, created_at"),
        @Index(name = "idx_messages_id_receiver_created_at",
                columnList = "id_receiver, created_at"),
        @Index(name = "idx_messages_id_property",
                columnList = "id_property")})
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sender", nullable = false)
    private Users idSender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_receiver", nullable = false)
    private Users idReceiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_property")
    private Properties idProperty;

    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @ColumnDefault("false")
    @Column(name = "is_read")
    private Boolean isRead;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;
}