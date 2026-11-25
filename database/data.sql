USE milsabores;

-- =====================================================
-- PROYECTO: Pasteleria Mil Sabores
-- EVALUACION: Parcial 3 - Desarrollo Fullstack II
-- DATOS DE PRUEBA
-- =====================================================

-- 1. CATEGORIAS

INSERT INTO categoria (nombre, descripcion)
VALUES
('Tortas Cuadradas', 'Tortas de formato cuadrado'),
('Tortas Circulares', 'Tortas de formato circular'),
('Postres Individuales', 'Postres en porciones individuales'),
('Productos Sin Azucar', 'Productos sin azucar añadida'),
('Pasteleria Tradicional', 'Productos tradicionales'),
('Productos Sin Gluten', 'Productos sin gluten'),
('Productos Veganos', 'Productos de origen vegetal'),
('Tortas Especiales', 'Tortas para celebraciones');

-- 2. PRODUCTOS

INSERT INTO producto
(codigo, nombre, descripcion, precio, stock, categoria_id)
VALUES
('TC001', 'Torta Cuadrada de Chocolate',
'Torta con relleno de chocolate', 45000, 10,
(SELECT id FROM categoria WHERE nombre = 'Tortas Cuadradas')),

('TC002', 'Torta Cuadrada de Frutas',
'Torta con frutas y crema', 50000, 8,
(SELECT id FROM categoria WHERE nombre = 'Tortas Cuadradas')),

('TT001', 'Torta Circular de Vainilla',
'Torta de vainilla con crema', 40000, 12,
(SELECT id FROM categoria WHERE nombre = 'Tortas Circulares')),

('PI001', 'Mousse de Chocolate',
'Postre individual de chocolate', 5000, 20,
(SELECT id FROM categoria WHERE nombre = 'Postres Individuales')),

('PSA001', 'Torta Sin Azucar',
'Torta sin azucar añadida', 48000, 5,
(SELECT id FROM categoria WHERE nombre = 'Productos Sin Azucar')),

('PT001', 'Empanada de Manzana',
'Empanada dulce tradicional', 3000, 30,
(SELECT id FROM categoria WHERE nombre = 'Pasteleria Tradicional')),

('PSG001', 'Brownie Sin Gluten',
'Brownie de chocolate sin gluten', 4000, 15,
(SELECT id FROM categoria WHERE nombre = 'Productos Sin Gluten')),

('PV001', 'Torta Vegana de Chocolate',
'Torta elaborada con ingredientes vegetales', 45000, 7,
(SELECT id FROM categoria WHERE nombre = 'Productos Veganos')),

('TE001', 'Torta de Cumpleaños',
'Torta especial para celebraciones', 55000, 6,
(SELECT id FROM categoria WHERE nombre = 'Tortas Especiales'));

-- 3. USUARIOS DE PRUEBA

INSERT INTO usuario
(run, nombres, apellidos, correo, password_hash,
region, comuna, direccion, rol)
VALUES
('111111111', 'Administrador', 'Prueba',
'admin@example.com', '$2a$10$dM/RGVd/EfHHuKUGegwuueHQZreqU0347S6SSUfPleyGw.NT9cHKm',
'Metropolitana', 'Melipilla', 'Direccion ficticia 1', 'ADMIN'),

('222222222', 'Vendedor', 'Prueba',
'vendedor@example.com', '$2a$10$6Md5M50.aJ6p/UEBbvXfB.4PZh6aN1It/p9Gur8K11uIQUnisO0Vy',
'Metropolitana', 'Melipilla', 'Direccion ficticia 2', 'VENDEDOR'),

('333333333', 'Cliente', 'Prueba',
'cliente@example.com', '$2a$10$2.RpgbSCP8aoB8s7w0L2u.szKYDnppdrYEAksHs5.gAivHPPqjH1K',
'Metropolitana', 'Melipilla', 'Direccion ficticia 3', 'CLIENTE');

-- 4. BOLETA DE PRUEBA

INSERT INTO boleta (usuario_id, total, estado)
VALUES (
    (SELECT id FROM usuario
     WHERE correo = 'cliente@example.com'),
    55000,
    'CONFIRMADA'
);

SET @boleta_id = LAST_INSERT_ID();

-- 5. DETALLES DE BOLETA

INSERT INTO detalle_boleta
(boleta_id, producto_id, cantidad, precio_unitario)
VALUES
(@boleta_id,
(SELECT id FROM producto WHERE codigo = 'TC001'),
1, 45000),

(@boleta_id,
(SELECT id FROM producto WHERE codigo = 'PI001'),
2, 5000);

-- 6. VERIFICACION DE REGISTROS

SELECT 'Usuarios' AS tabla, COUNT(*) AS registros FROM usuario
UNION ALL
SELECT 'Categorias', COUNT(*) FROM categoria
UNION ALL
SELECT 'Productos', COUNT(*) FROM producto
UNION ALL
SELECT 'Boletas', COUNT(*) FROM boleta
UNION ALL
SELECT 'Detalles', COUNT(*) FROM detalle_boleta;