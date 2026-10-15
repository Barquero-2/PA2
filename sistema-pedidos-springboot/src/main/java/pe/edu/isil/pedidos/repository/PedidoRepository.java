package pe.edu.isil.pedidos.repository;
import pe.edu.isil.pedidos.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PedidoRepository extends JpaRepository<Pedido, Long> { }