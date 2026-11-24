package cl.duoc.milsabores.backend.dto;

import jakarta.validation.constraints.*;

public record DetalleBoletaRequest(

    @NotNull(message = "El producto es obligatorio")
    @Positive
    Long productoId,

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    Integer cantidad

) {}