package cl.duoc.milsabores.backend.repository;
import cl.duoc.milsabores.backend.entity.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("select r from RefreshToken r where r.hash = :hash")
 Optional<RefreshToken> bloquear(@Param("hash") String hash);
}
