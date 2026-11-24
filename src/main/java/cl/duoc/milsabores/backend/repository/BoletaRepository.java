package cl.duoc.milsabores.backend.repository;

import cl.duoc.milsabores.backend.entity.Boleta;
import cl.duoc.milsabores.backend.entity.EstadoBoleta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoletaRepository
        extends JpaRepository<Boleta, Long> {

    List<Boleta> findByUsuario_Id(Long usuarioId);

    List<Boleta> findByEstado(EstadoBoleta estado);

    List<Boleta> findAllByOrderByFechaDesc();
}