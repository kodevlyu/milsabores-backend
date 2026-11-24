package cl.duoc.milsabores.backend.controller;

import cl.duoc.milsabores.backend.dto.*;
import cl.duoc.milsabores.backend.service.AuthService;
import cl.duoc.milsabores.backend.service.UsuarioService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticacion")
public class AuthController {

    private final AuthService authService;
    private final cl.duoc.milsabores.backend.service.RefreshTokenService refresh;
    private final UsuarioService usuarioService;

    public AuthController(
            AuthService authService,
            UsuarioService usuarioService, cl.duoc.milsabores.backend.service.RefreshTokenService refresh) {

        this.authService = authService;
        this.refresh = refresh;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion y obtener JWT")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
            authService.login(request)
        );
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar un nuevo cliente")
    public UsuarioResponse registrar(
            @Valid @RequestBody UsuarioRequest request) {

        return usuarioService.registrar(request);
    }
    @PostMapping("/refresh")
    @Operation(summary="Renovar y rotar token de sesion")
    public ResponseEntity<AuthResponse> renovar(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok().header("Cache-Control","no-store").body(refresh.renovar(request.refreshToken()));
    }
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cerrar(@Valid @RequestBody RefreshRequest request) { refresh.cerrar(request.refreshToken()); }
}
