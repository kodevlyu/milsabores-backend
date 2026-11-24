package cl.duoc.milsabores.backend.dto;

import jakarta.validation.constraints.*;

public record UsuarioRequest(

    @NotBlank(message = "El RUN es obligatorio")
    @Size(min = 7, max = 12)
    String run,

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    String nombres,

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    String apellidos,

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no es valido")
    @Size(max = 150)
    String correo,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 72)
    String password,

    @NotBlank(message = "La region es obligatoria")
    @Size(max = 100)
    String region,

    @NotBlank(message = "La comuna es obligatoria")
    @Size(max = 100)
    String comuna,

    @Size(max = 255)
    String direccion

) {}