package cl.duoc.milsabores.backend.dto;

import jakarta.validation.constraints.*;

public record CategoriaRequest(

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    String nombre,

    @Size(max = 255)
    String descripcion,

    Boolean activo

) {}