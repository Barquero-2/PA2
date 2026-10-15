-- Esquema mínimo de referencia. Si la BD del curso ya tiene tabla de productos,
-- adaptar @Table/@Column a esa tabla; no crear una tabla paralela sin revisar el modelo existente.
CREATE TABLE ProductoInventario (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(60) NOT NULL UNIQUE,
    stock INT NOT NULL CHECK (stock >= 0),
    version BIGINT NOT NULL DEFAULT 0
);
INSERT INTO ProductoInventario(codigo, stock) VALUES ('P001', 10), ('P002', 5);
