package numberlink.service.jpa;

import numberlink.dto.score.response.ScoreResponseDto;
import numberlink.dto.score.response.ScoreResponseSelfDto;
import numberlink.entity.UserEntity;
import numberlink.exceptions.ScoreException;

import java.util.List;
import java.util.UUID;

public interface ScoreService {
    List<ScoreResponseDto> getTopScoresByCriterion(String criterion, String period, String mapSize) throws ScoreException;

    ScoreResponseSelfDto getTopScore(UUID userId, String username);

    void addScore(UserEntity user, int elapsedSeconds, int width, int height);
}
