package cl.duoc.milsabores.backend.dto;

public record AuthResponse(

    String token,
    String tipo,
    String correo,
    String rol,
    long expiracion

) {
}