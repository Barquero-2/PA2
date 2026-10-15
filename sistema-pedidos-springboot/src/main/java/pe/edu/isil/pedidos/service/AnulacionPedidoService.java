package pe.edu.isil.pedidos.service;
import pe.edu.isil.pedidos.domain.AnulacionPedido;
import java.util.List;
public interface AnulacionPedidoService {
 AnulacionPedido anular(Long pedidoId, String motivo);
 List<AnulacionPedido> listar();
}