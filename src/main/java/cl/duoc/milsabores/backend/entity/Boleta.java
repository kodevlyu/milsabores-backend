package cl.duoc.milsabores.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boleta")
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoBoleta estado = EstadoBoleta.PENDIENTE;

    @OneToMany(
        mappedBy = "boleta",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<DetalleBoleta> detalles = new ArrayList<>();

    public Boleta() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoBoleta getEstado() { return estado; }
    public void setEstado(EstadoBoleta estado) {
        this.estado = estado;
    }

    public List<DetalleBoleta> getDetalles() {
        return detalles;
    }

    public void agregarDetalle(DetalleBoleta detalle) {
        detalles.add(detalle);
        detalle.setBoleta(this);
    }

    public void eliminarDetalle(DetalleBoleta detalle) {
        detalles.remove(detalle);
        detalle.setBoleta(null);
    }
}