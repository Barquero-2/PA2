package pe.edu.isil.pedidos.dto;
import pe.edu.isil.pedidos.domain.AnulacionPedido;
import java.time.LocalDateTime;
public record AnulacionPedidoResponse(Long id, Long pedidoId, String productoCodigo, int cantidadDevuelta, LocalDateTime fecha, String motivo) {
 public static AnulacionPedidoResponse from(AnulacionPedido a) { return new AnulacionPedidoResponse(a.getId(),a.getPedidoId(),a.getProductoCodigo(),a.getCantidadDevuelta(),a.getFecha(),a.getMotivo()); }
}