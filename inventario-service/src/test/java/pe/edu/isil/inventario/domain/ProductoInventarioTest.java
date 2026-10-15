package pe.edu.isil.inventario.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductoInventarioTest {
    @Test void devolucionValidaIncrementaStockExactamente() {
        ProductoInventario producto = new ProductoInventario("P001", 8);
        int restante = producto.devolverStock(2);
        assertEquals(10, restante);
        assertEquals(10, producto.getStock());
    }
    @Test void cantidadCeroONegativaSeRechazaSinCambiarStock() {
        ProductoInventario producto = new ProductoInventario("P001", 8);
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> producto.devolverStock(0)),
            () -> assertThrows(IllegalArgumentException.class, () -> producto.devolverStock(-2)),
            () -> assertEquals(8, producto.getStock())
        );
    }
}