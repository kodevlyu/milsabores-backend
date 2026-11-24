package cl.duoc.milsabores.backend.controller;

import cl.duoc.milsabores.backend.dto.*;
import cl.duoc.milsabores.backend.entity.Usuario;
import cl.duoc.milsabores.backend.repository.UsuarioRepository;
import cl.duoc.milsabores.backend.service.BoletaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boletas")
@Tag(name = "Boletas")
public class BoletaController {

    private final BoletaService service;
    private final UsuarioRepository usuarioRepository;

    public BoletaController(
            BoletaService service,
            UsuarioRepository usuarioRepository) {

        this.service = service;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoletaResponse crear(
            @Valid @RequestBody BoletaRequest request,
            Authentication authentication) {

        Usuario usuario = obtenerUsuario(authentication);

        return service.crear(usuario.getId(), request);
    }

    @GetMapping
    public List<BoletaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/mias")
    public List<BoletaResponse> listarMisCompras(
            Authentication authentication) {

        Usuario usuario = obtenerUsuario(authentication);

        return service.listarPorUsuario(usuario.getId());
    }

    @GetMapping("/{id}")
    public BoletaResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    private Usuario obtenerUsuario(
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Usuario no autenticado"
            );
        }

        return usuarioRepository
            .findByCorreo(authentication.getName())
            .filter(Usuario::isActivo)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario no autenticado"
                )
            );
    }
}