package cl.duoc.milsabores.backend.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoRequest(

    @NotBlank(message = "El codigo es obligatorio")
    @Size(max = 30)
    String codigo,

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    String nombre,

    String descripcion,

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01")
    @Digits(integer = 10, fraction = 2)
    BigDecimal precio,

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    Integer stock,

    @Size(max = 500)
    String imagen,

    Boolean disponible,

    @NotNull(message = "La categoria es obligatoria")
    @Positive
    Long categoriaId

) {}