USE milsabores;

START TRANSACTION;

-- Normalizar nombres de categorias.

UPDATE categoria
SET nombre = 'Productos Sin Azúcar'
WHERE nombre = 'Productos Sin Azucar';

UPDATE categoria
SET nombre = 'Pastelería Tradicional'
WHERE nombre = 'Pasteleria Tradicional';

-- Liberar codigos que corresponden a productos diferentes.
-- Se mantienen los ID y las relaciones con las boletas.

UPDATE producto
SET codigo = 'LEG-PI001'
WHERE codigo = 'PI001'
AND nombre = 'Mousse de Chocolate';

UPDATE producto
SET codigo = 'LEG-PT001'
WHERE codigo = 'PT001'
AND nombre = 'Empanada de Manzana';

UPDATE producto
SET codigo = 'LEG-TC002'
WHERE codigo = 'TC002'
AND nombre = 'Torta Cuadrada de Frutas';

-- Normalizar codigos de productos equivalentes.

UPDATE producto
SET codigo = 'TR001'
WHERE codigo = 'TT001';

UPDATE producto
SET codigo = 'VG001'
WHERE codigo = 'PV001';

-- Recuperar productos originales de la EP2.
-- Las existencias de productos nuevos son datos de prueba.
-- Los productos existentes conservan su stock.

INSERT INTO producto
(codigo, nombre, descripcion, precio, stock,
 imagen, disponible, categoria_id)
VALUES

('TC001', 'Torta Cuadrada de Chocolate',
'Torta de chocolate con ganache y avellanas.',
45000, 10, '/img/tc001.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Tortas Cuadradas')),

('TC002', 'Torta Cuadrada Frutos Rojos',
'Bizcocho de vainilla con relleno de frutos rojos y crema.',
48000, 10, '/img/tc002.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Tortas Cuadradas')),

('TC003', 'Torta Cuadrada Tres Leches',
'Clásica torta tres leches con cobertura de merengue.',
50000, 10, '/img/tc003.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Tortas Cuadradas')),

('TR001', 'Torta Circular de Vainilla',
'Bizcocho de vainilla con relleno de crema pastelera.',
42000, 10, '/img/tr001.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Tortas Circulares')),

('TR002', 'Torta Circular de Naranja',
'Torta húmeda de naranja con glaseado cítrico.',
43000, 10, '/img/tr002.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Tortas Circulares')),

('TR003', 'Torta Red Velvet',
'Torta red velvet con crema de queso.',
52000, 10, '/img/tr003.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Tortas Circulares')),

('PI001', 'Cheesecake Individual',
'Porción individual de cheesecake con salsa de frambuesa.',
5500, 20, '/img/pi001.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Postres Individuales')),

('PI002', 'Mousse de Chocolate',
'Mousse de chocolate intenso con virutas de cacao.',
4800, 20, '/img/pi002.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Postres Individuales')),

('PI003', 'Tiramisú Individual',
'Clásico tiramisú en formato individual.',
6000, 20, '/img/pi003.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Postres Individuales')),

('SA001', 'Torta Sin Azúcar de Limón',
'Torta de limón endulzada con stevia.',
49000, 10, '/img/sa001.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Productos Sin Azúcar')),

('SA002', 'Brownie Sin Azúcar',
'Brownie de cacao sin azúcar añadida.',
5200, 20, '/img/sa002.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Productos Sin Azúcar')),

('PT001', 'Milhojas Tradicional',
'Milhojas con manjar y crema pastelera.',
38000, 10, '/img/pt001.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Pastelería Tradicional')),

('PT002', 'Brazo de Reina',
'Bizcocho enrollado relleno de manjar.',
35000, 10, '/img/pt002.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Pastelería Tradicional')),

('VG001', 'Torta Vegana de Chocolate',
'Torta vegana de chocolate con leche vegetal.',
53000, 10, '/img/vg001.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Productos Veganos')),

('GL001', 'Torta Sin Gluten de Almendra',
'Torta de harina de almendra, apta para celíacos.',
54000, 10, '/img/gl001.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Productos Sin Gluten')),

('ES001', 'Torta Especial Cumpleaños Infantil',
'Torta temática infantil con decoración personalizada.',
65000, 10, '/img/es001.jpg', TRUE,
(SELECT id FROM categoria WHERE nombre = 'Tortas Especiales'))

ON DUPLICATE KEY UPDATE
nombre = VALUES(nombre),
descripcion = VALUES(descripcion),
precio = VALUES(precio),
imagen = VALUES(imagen),
disponible = VALUES(disponible),
categoria_id = VALUES(categoria_id);

-- Ocultar productos antiguos del catalogo publico.
-- Se conservan para mantener el historial de compras.

UPDATE producto
SET disponible = FALSE
WHERE codigo IN (
    'LEG-PI001',
    'LEG-PT001',
    'LEG-TC002',
    'PSA001',
    'PSG001',
    'TE001'
);

COMMIT;

-- Verificar catalogo.

SELECT id, codigo, nombre, precio, stock,
       imagen, disponible
FROM producto
ORDER BY disponible DESC, codigo;

SELECT COUNT(*) AS productos_disponibles
FROM producto
WHERE disponible = TRUE;

-- Verificar historial de compras.

SELECT COUNT(*) AS boletas FROM boleta;

SELECT COUNT(*) AS detalles FROM detalle_boleta;