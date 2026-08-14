package numberlink.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Table(name = "email_change_requests")
@Entity(name = "EmailChangeRequest")
public class EmailChangeRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "current_email")
    private String currentEmail;

    @Column(name = "new_email", nullable = false)
    private String newEmail;

    @Column(name = "confirm_code_hash", nullable = false, length = 64)
    private String confirmCodeHash;

    @Column(name = "cancel_token_hash", unique = true, length = 64)
    private String cancelTokenHash;

    @Column(nullable = false)
    private int attempts = 0;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "last_sent_at", nullable = false)
    private Instant lastSentAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
