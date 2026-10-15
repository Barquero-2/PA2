package pe.edu.isil.pedidos.repository;
import pe.edu.isil.pedidos.domain.AnulacionPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface AnulacionPedidoRepository extends JpaRepository<AnulacionPedido, Long> {
    Optional<AnulacionPedido> findByPedidoId(Long pedidoId);
}