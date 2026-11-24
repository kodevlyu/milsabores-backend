USE milsabores;
CREATE TABLE IF NOT EXISTS refresh_token (
 hash VARCHAR(64) PRIMARY KEY,
 usuario_id BIGINT NOT NULL,
 expira_en DATETIME(6) NOT NULL,
 CONSTRAINT fk_refresh_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);
