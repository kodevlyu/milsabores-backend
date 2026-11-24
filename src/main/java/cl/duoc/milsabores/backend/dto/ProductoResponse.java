package cl.duoc.milsabores.backend.dto;

import java.math.BigDecimal;

public record ProductoResponse(
    Long id,
    String codigo,
    String nombre,
    String descripcion,
    BigDecimal precio,
    Integer stock,
    String imagen,
    boolean disponible,
    Long categoriaId,
    String categoriaNombre
) {}