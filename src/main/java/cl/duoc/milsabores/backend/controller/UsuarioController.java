package cl.duoc.milsabores.backend.controller;

import cl.duoc.milsabores.backend.dto.*;
import cl.duoc.milsabores.backend.entity.Rol;
import cl.duoc.milsabores.backend.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PatchMapping("/{id}/rol")
    public UsuarioResponse cambiarRol(
            @PathVariable Long id,
            @RequestParam Rol rol) {

        return service.cambiarRol(id, rol);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivar(@PathVariable Long id) {
        service.desactivar(id);
    }
}