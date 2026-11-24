package cl.duoc.milsabores.backend.repository;

import cl.duoc.milsabores.backend.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository
        extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Categoria> findByActivoTrue();
}