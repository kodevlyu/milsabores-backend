package cl.duoc.milsabores.backend.service;

import cl.duoc.milsabores.backend.dto.*;
import cl.duoc.milsabores.backend.entity.*;
import cl.duoc.milsabores.backend.repository.*;
import cl.duoc.milsabores.backend.exception.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public BoletaService(
            BoletaRepository boletaRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository) {

        this.boletaRepository = boletaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public BoletaResponse crear(
            Long usuarioId, BoletaRequest request) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "Usuario no encontrado"
                )
            );

        if (!usuario.isActivo()) {
            throw new ConflictoException(
                "El usuario esta desactivado"
            );
        }

        if (usuario.getRol() != Rol.CLIENTE) {
            throw new ConflictoException(
                "Solo los clientes pueden realizar compras"
            );
        }

        if (request.detalles() == null ||
                request.detalles().isEmpty()) {

            throw new SolicitudInvalidaException(
                "La compra no contiene productos"
            );
        }

        Map<Long, Integer> cantidades = new TreeMap<>();

        for (DetalleBoletaRequest detalle : request.detalles()) {

            if (detalle == null ||
                detalle.productoId() == null ||
                detalle.cantidad() == null ||
                detalle.cantidad() <= 0) {

                throw new SolicitudInvalidaException(
                    "Producto o cantidad invalida"
                );
            }

            try {
                cantidades.merge(
                    detalle.productoId(),
                    detalle.cantidad(),
                    Math::addExact
                );
            } catch (ArithmeticException e) {
                throw new SolicitudInvalidaException(
                    "La cantidad solicitada es demasiado grande"
                );
            }
        }

        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);

        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> item :
                cantidades.entrySet()) {

            Producto producto = productoRepository
                .buscarPorIdConBloqueo(item.getKey())
                .orElseThrow(() ->
                    new RecursoNoEncontradoException(
                        "Producto no encontrado"
                    )
                );

            int cantidad = item.getValue();

            if (!producto.isDisponible() ||
                    !producto.getCategoria().isActivo()) {

                throw new ConflictoException(
                    "Producto no disponible: " +
                    producto.getNombre()
                );
            }

            if (producto.getStock() < cantidad) {
                throw new ConflictoException(
                    "Stock insuficiente para: " +
                    producto.getNombre()
                );
            }

            DetalleBoleta detalle = new DetalleBoleta();

            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());

            BigDecimal subtotal = producto.getPrecio()
                .multiply(BigDecimal.valueOf(cantidad));

            total = total.add(subtotal);

            producto.setStock(
                producto.getStock() - cantidad
            );

            productoRepository.save(producto);

            boleta.agregarDetalle(detalle);
        }

        if (total.compareTo(
                new BigDecimal("9999999999.99")) > 0) {

            throw new SolicitudInvalidaException(
                "El total supera el monto permitido"
            );
        }

        boleta.setTotal(total);
        boleta.setEstado(EstadoBoleta.CONFIRMADA);

        Boleta guardada = boletaRepository.save(boleta);

        return convertir(guardada);
    }

    @Transactional(readOnly = true)
    public List<BoletaResponse> listar() {

        return boletaRepository.findAllByOrderByFechaDesc()
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<BoletaResponse> listarPorUsuario(
            Long usuarioId) {

        return boletaRepository.findByUsuario_Id(usuarioId)
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Transactional(readOnly = true)
    public BoletaResponse buscarPorId(Long id) {

        Boleta boleta = boletaRepository.findById(id)
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "Boleta no encontrada"
                )
            );

        return convertir(boleta);
    }

    private BoletaResponse convertir(Boleta b) {

        List<DetalleBoletaResponse> detalles =
            b.getDetalles()
                .stream()
                .map(d -> new DetalleBoletaResponse(
                    d.getId(),
                    d.getProducto().getId(),
                    d.getProducto().getNombre(),
                    d.getCantidad(),
                    d.getPrecioUnitario(),
                    d.getSubtotal()
                ))
                .toList();

        return new BoletaResponse(
            b.getId(),
            b.getUsuario().getId(),
            b.getUsuario().getNombres() + " " +
                b.getUsuario().getApellidos(),
            b.getFecha(),
            b.getTotal(),
            b.getEstado(),
            detalles
        );
    }
}