package numberlink.dto.comment.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CommentResponseDto {
    private String player;
    private String avatarUrl;
    private String comment;
    private Instant commentedOn;
    private int rating;
}
