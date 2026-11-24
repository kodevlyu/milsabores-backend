package cl.duoc.milsabores.backend.controller;

import cl.duoc.milsabores.backend.dto.*;
import cl.duoc.milsabores.backend.service.CategoriaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar categorias activas")
    public List<CategoriaResponse> listar() {
        return service.listarActivas();
    }

    @GetMapping("/administracion")
    @Operation(summary = "Listar todas las categorias")
    public List<CategoriaResponse> listarAdministracion() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar categoria por ID")
    public CategoriaResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear categoria")
    public CategoriaResponse crear(
            @Valid @RequestBody CategoriaRequest request) {

        return service.crear(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoria")
    public CategoriaResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {

        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desactivar categoria")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}