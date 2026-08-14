package numberlink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Table(name = "user_sessions")
@Entity(name = "UserSession")
public class UserSessionEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "http_session_hash", nullable = false, unique = true, length = 64)
    private String httpSessionHash;

    @Column(nullable = false, length = 128)
    private String device;

    @Column(nullable = false, length = 64)
    private String os;

    @Column(nullable = false, length = 64)
    private String browser;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "last_seen_at", nullable = false)
    private Instant lastSeenAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;
}
