package numberlink.dto.score.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ScoreResponseSelfDto {
    private String player;
    private String avatarUrl;
    private BigDecimal avgElapsedSeconds;
    private BigDecimal avgScore;
    private Integer points;
    private Long rank;

    public ScoreResponseSelfDto(
            String player,
            BigDecimal avgElapsedSeconds,
            BigDecimal avgScore,
            Integer points,
            Long rank
    ) {
        this.player = player;
        this.avgElapsedSeconds = avgElapsedSeconds;
        this.avgScore = avgScore;
        this.points = points;
        this.rank = rank;
    }
}
