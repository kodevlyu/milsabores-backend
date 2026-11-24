package cl.duoc.milsabores.backend.controller;

import cl.duoc.milsabores.backend.dto.*;
import cl.duoc.milsabores.backend.service.ProductoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoResponse> listar() {
        return service.listarDisponibles();
    }

    @GetMapping("/{id}")
    public ProductoResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse crear(
            @Valid @RequestBody ProductoRequest request) {

        return service.crear(request);
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {

        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}