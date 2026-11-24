package cl.duoc.milsabores.backend.repository;

import cl.duoc.milsabores.backend.entity.Producto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository
        extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Producto> findByDisponibleTrue();

    List<Producto> findByCategoria_Id(Long categoriaId);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Producto p WHERE p.id = :id")
    Optional<Producto> buscarPorIdConBloqueo(
        @Param("id") Long id
    );
}