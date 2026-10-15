package pe.edu.isil.inventario.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "ProductoInventario")
public class ProductoInventario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codigo", nullable = false, unique = true, length = 60)
    private String codigo;
    @Column(name = "stock", nullable = false)
    private int stock;
    @Version
    @Column(name = "version")
    private long version;

    protected ProductoInventario() { }
    public ProductoInventario(String codigo, int stock) {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("El código es obligatorio");
        if (stock < 0) throw new IllegalArgumentException("El stock no puede ser negativo");
        this.codigo = codigo; this.stock = stock;
    }
    public int devolverStock(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad devuelta debe ser mayor que cero");
        this.stock = Math.addExact(this.stock, cantidad);
        return this.stock;
    }
    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public int getStock() { return stock; }
    public long getVersion() { return version; }
}