package pe.edu.isil.pedidos.domain;

import jakarta.persistence.*;

@Entity
@Table(name="pedidos")
public class Pedido {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=60)
    private String productoCodigo;
    @Column(nullable=false)
    private int cantidad;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20)
    private PedidoEstado estado = PedidoEstado.REGISTRADO;
    protected Pedido() { }
    public Pedido(String productoCodigo, int cantidad) {
        if (productoCodigo == null || productoCodigo.isBlank()) throw new IllegalArgumentException("productoCodigo es obligatorio");
        if (cantidad <= 0) throw new IllegalArgumentException("cantidad debe ser mayor que cero");
        this.productoCodigo=productoCodigo; this.cantidad=cantidad; this.estado=PedidoEstado.REGISTRADO;
    }
    public void anular() { if (estado != PedidoEstado.REGISTRADO) throw new IllegalStateException("Solo se puede anular un pedido REGISTRADO"); estado=PedidoEstado.ANULADO; }
    public Long getId() { return id; } public String getProductoCodigo() { return productoCodigo; }
    public int getCantidad() { return cantidad; } public PedidoEstado getEstado() { return estado; }
}