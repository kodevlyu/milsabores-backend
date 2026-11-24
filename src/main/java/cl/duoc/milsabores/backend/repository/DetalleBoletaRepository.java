package cl.duoc.milsabores.backend.repository;

import cl.duoc.milsabores.backend.entity.DetalleBoleta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetalleBoletaRepository
        extends JpaRepository<DetalleBoleta, Long> {

    List<DetalleBoleta> findByBoleta_Id(Long boletaId);

    List<DetalleBoleta> findByProducto_Id(Long productoId);
}