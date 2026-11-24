package cl.duoc.milsabores.backend.service;
import cl.duoc.milsabores.backend.entity.*;
import cl.duoc.milsabores.backend.repository.RefreshTokenRepository;
import cl.duoc.milsabores.backend.security.JwtService;
import cl.duoc.milsabores.backend.dto.AuthResponse;
import java.security.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Service
public class RefreshTokenService {
 private final RefreshTokenRepository repo;
 private final JwtService jwt;
 private final SecureRandom random = new SecureRandom();
 public RefreshTokenService(RefreshTokenRepository repo, JwtService jwt) {this.repo=repo;this.jwt=jwt;}
 private String hash(String value) {try {return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));} catch(NoSuchAlgorithmException e) {throw new IllegalStateException(e);}}
 private AuthResponse emitir(Usuario u, LocalDateTime limite) {
  byte[] bytes=new byte[32]; random.nextBytes(bytes);
  String raw=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  RefreshToken r=new RefreshToken(); r.hash=hash(raw);r.usuario=u;r.expiraEn=limite;repo.save(r);
  return new AuthResponse(jwt.generarToken(u.getCorreo(),u.getRol().name()),"Bearer",u.getCorreo(),u.getRol().name(),jwt.getExpiration(),raw);
 }
 @Transactional public AuthResponse iniciar(Usuario u) {return emitir(u,LocalDateTime.now().plusDays(7));}
 @Transactional public AuthResponse renovar(String raw) {
  RefreshToken r=repo.bloquear(hash(raw)).orElseThrow(this::invalido);
  if(!r.expiraEn.isAfter(LocalDateTime.now()) || !r.usuario.isActivo()) throw invalido();
  Usuario u=r.usuario; LocalDateTime limite=r.expiraEn;repo.delete(r);return emitir(u,limite);
 }
 @Transactional public void cerrar(String raw) {repo.bloquear(hash(raw)).ifPresent(repo::delete);}
 private ResponseStatusException invalido() {return new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sesion invalida o vencida");}
}
