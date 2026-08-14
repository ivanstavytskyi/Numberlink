package numberlink.repository;

import numberlink.entity.OauthUserEntity;
import numberlink.entity.enums.OauthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OauthUserRepository extends JpaRepository<OauthUserEntity, Long> {
    Optional<OauthUserEntity> findByProviderAndSub(OauthProvider provider, String sub);

    Optional<OauthUserEntity> findByUser_IdAndProvider(UUID userId, OauthProvider provider);

    List<OauthUserEntity> findByUser_Id(UUID userId);

    boolean existsByUser_IdAndProvider(UUID userId, OauthProvider provider);

    long countByUser_Id(UUID userId);
}
