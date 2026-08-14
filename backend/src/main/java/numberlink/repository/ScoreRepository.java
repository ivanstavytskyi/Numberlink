package numberlink.repository;

import numberlink.entity.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScoreRepository extends JpaRepository<ScoreEntity, Long> {
    List<ScoreEntity> findAllByOrderByScoreResultDesc();

    Optional<ScoreEntity> findFirstByUser_IdOrderByScoreResultDesc(UUID userId);
}
