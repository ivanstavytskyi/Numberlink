package numberlink.dto.rating.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequestDto {
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
