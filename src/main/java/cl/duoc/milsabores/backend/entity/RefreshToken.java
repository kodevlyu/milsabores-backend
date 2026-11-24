package cl.duoc.milsabores.backend.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="refresh_token")
public class RefreshToken {
 @Id @Column(length=64) public String hash;
 @ManyToOne(optional=false) @JoinColumn(name="usuario_id") public Usuario usuario;
 @Column(name="expira_en",nullable=false) public LocalDateTime expiraEn;
}
