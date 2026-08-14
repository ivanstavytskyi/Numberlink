package numberlink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Table(name = "user_totp")
@Entity(name = "UserTotp")
public class UserTotpEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(length = 64)
    private String secret;

    @Column(name = "pending_secret", length = 64)
    private String pendingSecret;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;
}
