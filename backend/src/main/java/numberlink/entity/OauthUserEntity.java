package numberlink.entity;

import numberlink.entity.enums.OauthProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.Instant;

@Getter
@Setter
@ToString
@Table(name="oauth_accounts")
@Entity(name="OauthUser")
public class OauthUserEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private OauthProvider provider;

    @Column(nullable = false)
    private String sub;

    @Column(name = "display_name", length = 255)
    private String displayName;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;
}
