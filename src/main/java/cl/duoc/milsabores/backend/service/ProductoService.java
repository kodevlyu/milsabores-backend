package cl.duoc.milsabores.backend.service;

import cl.duoc.milsabores.backend.dto.*;
import cl.duoc.milsabores.backend.entity.*;
import cl.duoc.milsabores.backend.repository.*;
import cl.duoc.milsabores.backend.exception.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(
            ProductoRepository repository,
            CategoriaRepository categoriaRepository) {

        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return repository.findAll()
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarDisponibles() {
        return repository.findByDisponibleTrue()
            .stream()
            .filter(p -> p.getCategoria().isActivo())
            .map(this::convertir)
            .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse buscarPorId(Long id) {
        return convertir(obtenerEntidad(id));
    }

    public ProductoResponse crear(ProductoRequest request) {

        if (repository.existsByCodigo(request.codigo())) {
            throw new ConflictoException(
                "El codigo del producto ya existe"
            );
        }

        Producto producto = new Producto();

        asignarDatos(producto, request);

        return convertir(repository.save(producto));
    }

    public ProductoResponse actualizar(
            Long id, ProductoRequest request) {

        Producto producto = obtenerConBloqueo(id);

        repository.findByCodigo(request.codigo())
            .filter(p -> !p.getId().equals(id))
            .ifPresent(p -> {
                throw new ConflictoException(
                    "El codigo ya esta registrado"
                );
            });

        asignarDatos(producto, request);

        return convertir(repository.save(producto));
    }

    public void eliminar(Long id) {
        Producto producto = obtenerConBloqueo(id);
        producto.setDisponible(false);
        repository.save(producto);
    }

    private void asignarDatos(
            Producto producto,
            ProductoRequest request) {

        Categoria categoria = categoriaRepository
            .findById(request.categoriaId())
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "Categoria no encontrada"
                )
            );

        if (!categoria.isActivo()) {
            throw new ConflictoException(
                "La categoria esta desactivada"
            );
        }

        producto.setCodigo(request.codigo());
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setImagen(request.imagen());
        producto.setCategoria(categoria);

        if (request.disponible() != null) {
            producto.setDisponible(request.disponible());
        }
    }

    private Producto obtenerEntidad(Long id) {
        return repository.findById(id)
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "Producto no encontrado"
                )
            );
    }

    private Producto obtenerConBloqueo(Long id) {
        return repository.buscarPorIdConBloqueo(id)
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "Producto no encontrado"
                )
            );
    }

    private ProductoResponse convertir(Producto p) {
        return new ProductoResponse(
            p.getId(),
            p.getCodigo(),
            p.getNombre(),
            p.getDescripcion(),
            p.getPrecio(),
            p.getStock(),
            p.getImagen(),
            p.isDisponible(),
            p.getCategoria().getId(),
            p.getCategoria().getNombre()
        );
    }
}