package cl.duoc.milsabores.backend.service;

import cl.duoc.milsabores.backend.dto.*;
import cl.duoc.milsabores.backend.entity.*;
import cl.duoc.milsabores.backend.repository.UsuarioRepository;
import cl.duoc.milsabores.backend.exception.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return repository.findAll()
            .stream()
            .map(this::convertir)
            .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return convertir(obtenerEntidad(id));
    }

    public UsuarioResponse registrar(UsuarioRequest request) {

        String correo = request.correo()
            .trim()
            .toLowerCase(Locale.ROOT);

        String run = request.run()
            .replace(".", "")
            .replace("-", "")
            .trim()
            .toUpperCase(Locale.ROOT);

        if (repository.existsByCorreo(correo)) {
            throw new ConflictoException(
                "El correo ya esta registrado"
            );
        }

        if (repository.existsByRun(run)) {
            throw new ConflictoException(
                "El RUN ya esta registrado"
            );
        }

        if (request.password()
                .getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new SolicitudInvalidaException(
                "La contraseña supera el limite permitido"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setRun(run);
        usuario.setNombres(request.nombres());
        usuario.setApellidos(request.apellidos());
        usuario.setCorreo(correo);

        usuario.setPasswordHash(
            passwordEncoder.encode(request.password())
        );

        usuario.setRegion(request.region());
        usuario.setComuna(request.comuna());
        usuario.setDireccion(request.direccion());
        usuario.setRol(Rol.CLIENTE);
        usuario.setActivo(true);

        return convertir(repository.save(usuario));
    }

    public void desactivar(Long id) {
        Usuario usuario = obtenerEntidad(id);
        usuario.setActivo(false);
        repository.save(usuario);
    }

    public UsuarioResponse cambiarRol(Long id, Rol rol) {

        if (rol == null) {
            throw new SolicitudInvalidaException(
                "El rol es obligatorio"
            );
        }

        Usuario usuario = obtenerEntidad(id);
        usuario.setRol(rol);

        return convertir(repository.save(usuario));
    }

    public UsuarioResponse crearAdmin(UsuarioAdminRequest r) {
        UsuarioResponse creado = registrar(new UsuarioRequest(r.run(), r.nombres(), r.apellidos(), r.correo(), r.password(), r.region(), r.comuna(), r.direccion()));
        Usuario u = obtenerEntidad(creado.id());
        u.setRol(r.rol()); u.setActivo(r.activo());
        return convertir(repository.save(u));
    }

    public UsuarioResponse actualizar(Long id, UsuarioActualizarRequest r, String actor) {
        Usuario u = obtenerEntidad(id);
        proteger(u, actor);
        String correo = r.correo().trim().toLowerCase(Locale.ROOT);
        String run = r.run().replace(".", "").replace("-", "").trim().toUpperCase(Locale.ROOT);
        if (repository.findByCorreo(correo).filter(x -> !x.getId().equals(id)).isPresent() ||
            repository.findByRun(run).filter(x -> !x.getId().equals(id)).isPresent())
            throw new ConflictoException("RUN o correo ya registrado");
        if (r.password() != null && !r.password().isEmpty()) {
            if (r.password().length() < 8 || r.password().getBytes(StandardCharsets.UTF_8).length > 72)
                throw new SolicitudInvalidaException("Contraseña inválida");
            u.setPasswordHash(passwordEncoder.encode(r.password()));
        }
        u.setRun(run); u.setCorreo(correo); u.setNombres(r.nombres()); u.setApellidos(r.apellidos());
        u.setRegion(r.region()); u.setComuna(r.comuna()); u.setDireccion(r.direccion());
        u.setRol(r.rol()); u.setActivo(r.activo());
        return convertir(repository.save(u));
    }

    public void protegerCuenta(Long id, String actor) { proteger(obtenerEntidad(id), actor); }
    private void proteger(Usuario u, String actor) {
        if (u.getCorreo().equalsIgnoreCase(actor)) throw new ConflictoException("No puedes modificar tu propia cuenta desde este panel");
    }

    private Usuario obtenerEntidad(Long id) {
        return repository.findById(id)
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "Usuario no encontrado"
                )
            );
    }

    private UsuarioResponse convertir(Usuario u) {
        return new UsuarioResponse(
            u.getId(),
            u.getRun(),
            u.getNombres(),
            u.getApellidos(),
            u.getCorreo(),
            u.getRegion(),
            u.getComuna(),
            u.getDireccion(),
            u.getRol(),
            u.isActivo(),
            u.getFechaRegistro()
        );
    }
}
