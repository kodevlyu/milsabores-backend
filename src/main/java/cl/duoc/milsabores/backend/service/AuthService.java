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
    private final RefreshTokenService refresh;

    public AuthService(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            JwtService jwtService, RefreshTokenService refresh) {

        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.refresh = refresh;
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

        return refresh.iniciar(usuario);
    }
}
