
package cl.duoc.milsabores.backend.controller;

import cl.duoc.milsabores.backend.dto.*;
import cl.duoc.milsabores.backend.service.ProductoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // Listado público de productos disponibles.
    @GetMapping
    @Operation(summary = "Listar productos disponibles")
    public List<ProductoResponse> listar() {
        return service.listarDisponibles();
    }

    // Listado administrativo de todos los productos.
    @GetMapping("/administracion")
    @Operation(summary = "Listar todos los productos")
    public List<ProductoResponse> listarAdministracion() {
        return service.listar();
    }

    // Consultar un producto por su ID.
    @GetMapping("/{id}")
    @Operation(summary = "Consultar producto por ID")
    public ProductoResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // Crear un nuevo producto.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear producto")
    public ProductoResponse crear(
            @Valid @RequestBody ProductoRequest request) {

        return service.crear(request);
    }

    // Actualizar un producto existente.
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ProductoResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {

        return service.actualizar(id, request);
    }

    // Desactivar un producto mediante eliminación lógica.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desactivar producto")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
