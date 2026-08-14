package numberlink.repository;

import numberlink.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    // findById - method implemented by default in JpaRepository
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    Optional<UserEntity> findByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);

    @Query("SELECT u.sessionEpoch FROM User u WHERE u.id = :id")
    Optional<Integer> findSessionEpochById(@Param("id") UUID id);
}
