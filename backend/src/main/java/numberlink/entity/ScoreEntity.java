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
@Table(name="score")
@Entity(name="Score")
public class ScoreEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "score_result", nullable = false)
    private int scoreResult;

    @Column(name = "elapsed_seconds", nullable = false)
    private int elapsedSeconds;

    @Column(name = "field_width", nullable = false)
    private int fieldWidth;

    @Column(name = "field_height", nullable = false)
    private int fieldHeight;

    @Column(name = "played_at", nullable = false)
    private Instant playedAt;
}
