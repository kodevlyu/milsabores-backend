package cl.duoc.milsabores.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
    name = "detalle_boleta",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_detalle_producto",
            columnNames = {"boleta_id", "producto_id"}
        )
    }
)
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boleta_id", nullable = false)
    private Boleta boleta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(
        name = "precio_unitario",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal precioUnitario;

    public DetalleBoleta() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Boleta getBoleta() { return boleta; }
    public void setBoleta(Boleta boleta) {
        this.boleta = boleta;
    }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Transient
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(
            BigDecimal.valueOf(cantidad)
        );
    }
}