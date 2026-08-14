package numberlink.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@Table(name = "users")
@Entity(name = "User")
public class UserEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Version
    @Column(nullable = false)
    private Long version = 0L;

    @Column(unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "avatar_url", length = 512)
    private String avatarUrl;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Column(name = "email_verified_at")
    private Instant emailVerifiedAt ;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "session_epoch", nullable = false)
    private int sessionEpoch = 0;

}