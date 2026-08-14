package numberlink.service.jpa;

import numberlink.dto.score.response.ScoreResponseDto;
import numberlink.dto.score.response.ScoreResponseSelfDto;
import numberlink.entity.ScoreEntity;
import numberlink.entity.UserEntity;
import numberlink.exceptions.ScoreException;
import numberlink.repository.ScoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class ScoreServiceJPA implements ScoreService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ScoreRepository scoreRepository;

    public ScoreServiceJPA(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Override
    public List<ScoreResponseDto> getTopScoresByCriterion(String criterion, String period, String mapSize)
            throws ScoreException {
        StringBuilder jpql = new StringBuilder("""
                SELECT new numberlink.dto.score.response.ScoreResponseDto(
                    u.username,
                    u.avatarUrl,
                    MIN(s.elapsedSeconds),
                    MAX(s.fieldWidth),
                    MAX(s.fieldHeight),
                    ROUND(AVG(s.elapsedSeconds), 1),
                    ROUND(AVG(s.scoreResult), 1),
                    MAX(s.scoreResult)
                )
                FROM Score s JOIN s.user u
                WHERE 1 = 1
                """);

        Instant fromDate = null;
        if ("week".equalsIgnoreCase(period)) {
            fromDate = LocalDate.now().minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant();
            jpql.append(" AND s.playedAt >= :fromDate");
        } else if ("month".equalsIgnoreCase(period)) {
            fromDate = LocalDate.now().minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant();
            jpql.append(" AND s.playedAt >= :fromDate");
        }

        int width = 0;
        int height = 0;
        if (mapSize != null && mapSize.matches("\\d+x\\d+")) {
            String[] parts = mapSize.toLowerCase().split("x");
            width = Integer.parseInt(parts[0]);
            height = Integer.parseInt(parts[1]);
            jpql.append(" AND s.fieldWidth = :width AND s.fieldHeight = :height");
        }

        jpql.append(" GROUP BY u.username, u.avatarUrl");

        String sort = criterion == null ? "score" : criterion;
        switch (sort) {
            case "time" -> jpql.append(" ORDER BY MIN(s.elapsedSeconds) ASC");
            case "avgElapsedSeconds", "avgTime" -> jpql.append(" ORDER BY ROUND(AVG(s.elapsedSeconds), 1) ASC");
            case "avgScore" -> jpql.append(" ORDER BY ROUND(AVG(s.scoreResult), 1) DESC");
            default -> jpql.append(" ORDER BY MAX(s.scoreResult) DESC");
        }

        TypedQuery<ScoreResponseDto> query =
                entityManager.createQuery(jpql.toString(), ScoreResponseDto.class);

        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (width > 0 && height > 0) {
            query.setParameter("width", width);
            query.setParameter("height", height);
        }

        return query.setMaxResults(100).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public ScoreResponseSelfDto getTopScore(UUID userId, String username) {
        List<Object[]> rows = entityManager.createNativeQuery("""
                        SELECT
                            ROUND(AVG(s.elapsed_seconds)::numeric, 1) AS avg_duration,
                            ROUND(AVG(s.score_result)::numeric, 1) AS avg_points,
                            MAX(s.score_result) AS max_points,
                            (
                                SELECT COUNT(*) + 1
                                FROM (
                                    SELECT user_id, MAX(score_result) AS best
                                    FROM score
                                    GROUP BY user_id
                                ) rivals
                                WHERE rivals.best > (
                                    SELECT MAX(score_result) FROM score WHERE user_id = :userId
                                )
                            ) AS position
                        FROM score s
                        WHERE s.user_id = :userId
                        """)
                .setParameter("userId", userId)
                .getResultList();

        if (rows.isEmpty() || rows.get(0) == null || rows.get(0)[2] == null) {
            ScoreResponseSelfDto empty = new ScoreResponseSelfDto();
            empty.setPlayer(username);
            return empty;
        }

        Object[] row = rows.get(0);
        BigDecimal avgTime = row[0] != null ? new BigDecimal(row[0].toString()) : null;
        BigDecimal avgScore = row[1] != null ? new BigDecimal(row[1].toString()) : null;
        Integer points = row[2] != null ? ((Number) row[2]).intValue() : null;
        Long rank = row[3] != null ? ((Number) row[3]).longValue() : null;

        return new ScoreResponseSelfDto(username, avgTime, avgScore, points, rank);
    }

    @Override
    public void addScore(UserEntity user, int elapsedSeconds, int width, int height) {
        if (elapsedSeconds < 1) {
            elapsedSeconds = 1;
        }
        int points = Math.round(10000f / elapsedSeconds);

        ScoreEntity score = new ScoreEntity();
        score.setUser(user);
        score.setElapsedSeconds(elapsedSeconds);
        score.setFieldWidth(width);
        score.setFieldHeight(height);
        score.setScoreResult(points);
        score.setPlayedAt(Instant.now());
        scoreRepository.save(score);
    }
}
