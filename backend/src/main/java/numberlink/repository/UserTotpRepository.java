package numberlink.repository;

import numberlink.entity.UserTotpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserTotpRepository extends JpaRepository<UserTotpEntity, UUID> {
}
