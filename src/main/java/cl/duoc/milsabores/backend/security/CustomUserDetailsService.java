package cl.duoc.milsabores.backend.security;

import cl.duoc.milsabores.backend.entity.Usuario;
import cl.duoc.milsabores.backend.repository.UsuarioRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(
            UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
            .findByCorreo(correo)
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    "Usuario no encontrado"
                )
            );

        return User.builder()
            .username(usuario.getCorreo())
            .password(usuario.getPasswordHash())
            .roles(usuario.getRol().name())
            .disabled(!usuario.isActivo())
            .build();
    }
}