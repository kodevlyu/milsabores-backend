package cl.duoc.milsabores.backend.service;

import cl.duoc.milsabores.backend.dto.LoginRequest;
import cl.duoc.milsabores.backend.dto.AuthResponse;
import cl.duoc.milsabores.backend.entity.Usuario;
import cl.duoc.milsabores.backend.repository.UsuarioRepository;
import cl.duoc.milsabores.backend.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication
    .UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.correo(),
                    request.password()
                )
            );
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Correo o contrasena incorrectos"
            );
        }

        Usuario usuario = usuarioRepository
            .findByCorreo(request.correo())
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales incorrectas"
                )
            );

        String token = jwtService.generarToken(
            usuario.getCorreo(),
            usuario.getRol().name()
        );

        return new AuthResponse(
            token,
            "Bearer",
            usuario.getCorreo(),
            usuario.getRol().name(),
            jwtService.getExpiration()
        );
    }
}