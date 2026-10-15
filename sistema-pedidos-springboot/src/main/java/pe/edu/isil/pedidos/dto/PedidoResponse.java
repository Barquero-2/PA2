package pe.edu.isil.pedidos.dto;
import pe.edu.isil.pedidos.domain.Pedido;
import pe.edu.isil.pedidos.domain.PedidoEstado;
public record PedidoResponse(Long id, String productoCodigo, int cantidad, PedidoEstado estado) {
 public static PedidoResponse from(Pedido p) { return new PedidoResponse(p.getId(),p.getProductoCodigo(),p.getCantidad(),p.getEstado()); }
}