package numberlink.dto.score.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreRequestDto {
    @NotNull
    @Min(1)
    private Integer elapsedSeconds;
}
