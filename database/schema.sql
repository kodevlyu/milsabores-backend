-- =====================================================
-- PROYECTO: Pasteleria Mil Sabores
-- ASIGNATURA: Desarrollo Fullstack II
-- EVALUACION: Parcial 3
-- BASE DE DATOS: MySQL
-- =====================================================

CREATE DATABASE IF NOT EXISTS milsabores
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE milsabores;

-- =====================================================
-- 1. TABLA USUARIO
-- =====================================================

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    run VARCHAR(12) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    region VARCHAR(100) NOT NULL,
    comuna VARCHAR(100) NOT NULL,
    direccion VARCHAR(255),
    rol ENUM('ADMIN', 'VENDEDOR', 'CLIENTE')
        NOT NULL DEFAULT 'CLIENTE',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 2. TABLA CATEGORIA
-- =====================================================

CREATE TABLE IF NOT EXISTS categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- =====================================================
-- 3. TABLA PRODUCTO
-- =====================================================

CREATE TABLE IF NOT EXISTS producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(12,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    imagen VARCHAR(500),
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    categoria_id BIGINT NOT NULL,

    CONSTRAINT chk_producto_precio
        CHECK (precio > 0),

    CONSTRAINT chk_producto_stock
        CHECK (stock >= 0),

    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categoria(id)
);

-- =====================================================
-- 4. TABLA BOLETA
-- =====================================================

CREATE TABLE IF NOT EXISTS boleta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(12,2) NOT NULL,
    estado ENUM('PENDIENTE', 'CONFIRMADA', 'CANCELADA')
        NOT NULL DEFAULT 'PENDIENTE',

    CONSTRAINT chk_boleta_total
        CHECK (total >= 0),

    CONSTRAINT fk_boleta_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
);

-- =====================================================
-- 5. TABLA DETALLE BOLETA
-- =====================================================

CREATE TABLE IF NOT EXISTS detalle_boleta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    boleta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,

    CONSTRAINT chk_detalle_cantidad
        CHECK (cantidad > 0),

    CONSTRAINT chk_detalle_precio
        CHECK (precio_unitario > 0),

    CONSTRAINT uq_detalle_producto
        UNIQUE (boleta_id, producto_id),

    CONSTRAINT fk_detalle_boleta
        FOREIGN KEY (boleta_id)
        REFERENCES boleta(id),

    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (producto_id)
        REFERENCES producto(id)
);

-- =====================================================
-- 6. VERIFICACION DE TABLAS
-- =====================================================

CREATE TABLE IF NOT EXISTS refresh_token (
 hash VARCHAR(64) PRIMARY KEY,
 usuario_id BIGINT NOT NULL,
 expira_en DATETIME(6) NOT NULL,
 CONSTRAINT fk_refresh_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

SHOW TABLES;
