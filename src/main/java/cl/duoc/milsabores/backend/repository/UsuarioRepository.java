package cl.duoc.milsabores.backend.repository;

import cl.duoc.milsabores.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByRun(String run);

    boolean existsByCorreo(String correo);

    boolean existsByRun(String run);
}