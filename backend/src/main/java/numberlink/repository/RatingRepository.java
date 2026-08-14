package numberlink.repository;

import numberlink.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<RatingEntity, Long> {

    @Query("SELECT AVG(r.value) FROM Rating r")
    Double findAverageRating();

    Optional<RatingEntity> findByUser_Id(UUID userId);

    @Query("""
            SELECT r FROM Rating r
            JOIN FETCH r.user
            WHERE r.content IS NOT NULL AND TRIM(r.content) <> ''
            ORDER BY r.commentedOn DESC
            """)
    List<RatingEntity> findAllWithComments();

    @Query("""
            SELECT r.value,
                   (COUNT(r) * 100.0 / (SELECT COUNT(r2) FROM Rating r2))
            FROM Rating r
            GROUP BY r.value
            """)
    List<Object[]> getRatingPercentage();
}
