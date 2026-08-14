package numberlink.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@ToString
@Table(name="rating")
@Entity(name="Rating")
@NamedQuery (
    name="RatingEntity.getRatingPercentage",
    query= """
    SELECT r.value,
    (COUNT(r) * 100.0 / (SELECT COUNT(r2) FROM Rating r2))
    FROM Rating r
    GROUP BY r.value
    """
)
public class RatingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(nullable = false)
    private Integer value;

    @Column(name = "rated_at", nullable = false)
    private Instant ratedAt;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "commented_on")
    private Instant commentedOn;
}
