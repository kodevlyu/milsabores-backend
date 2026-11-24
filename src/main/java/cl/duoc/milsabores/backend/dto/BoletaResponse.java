package cl.duoc.milsabores.backend.dto;

import cl.duoc.milsabores.backend.entity.EstadoBoleta;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BoletaResponse(
    Long id,
    Long usuarioId,
    String cliente,
    LocalDateTime fecha,
    BigDecimal total,
    EstadoBoleta estado,
    List<DetalleBoletaResponse> detalles
) {}