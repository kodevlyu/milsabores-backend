package cl.duoc.milsabores.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(secret)
        );
    }

    public String generarToken(String correo, String rol) {
        return Jwts.builder()
            .subject(correo)
            .id(java.util.UUID.randomUUID().toString())
            .claim("rol", rol)
            .issuedAt(new Date())
            .expiration(
                new Date(System.currentTimeMillis() + expiration)
            )
            .signWith(getSigningKey())
            .compact();
    }

    public String extraerCorreo(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean validarToken(String token, String correo) {
        try {
            return extraerCorreo(token).equals(correo);
        } catch (RuntimeException e) {
            return false;
        }
    }

    public long getExpiration() {
        return expiration;
    }
}
