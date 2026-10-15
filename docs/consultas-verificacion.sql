-- Ejecutar en SQL Server / InventarioDB; adaptar nombres si se integra con el esquema real del curso.
SELECT codigo, stock, version FROM ProductoInventario WHERE codigo = 'P001';

-- Ejecutar en consola H2 (http://localhost:8081/sistema-pedidos/h2-console), JDBC URL:
-- jdbc:h2:mem:pedidosdb, usuario sa, contraseña vacía.
SELECT id, producto_codigo, cantidad, estado FROM pedidos ORDER BY id;
SELECT id, pedido_id, producto_codigo, cantidad_devuelta, fecha, motivo FROM anulaciones_pedido ORDER BY id;
SELECT COUNT(*) AS cantidad_anulaciones FROM anulaciones_pedido WHERE pedido_id = 1;
