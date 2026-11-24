package cl.duoc.milsabores.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record BoletaRequest(

    @NotEmpty(message = "La compra debe contener productos")
    List<@NotNull @Valid DetalleBoletaRequest> detalles

) {}