package cl.duoc.milsabores.backend.dto;

import cl.duoc.milsabores.backend.entity.Rol;
import java.time.LocalDateTime;

public record UsuarioResponse(
    Long id,
    String run,
    String nombres,
    String apellidos,
    String correo,
    String region,
    String comuna,
    String direccion,
    Rol rol,
    boolean activo,
    LocalDateTime fechaRegistro
) {}