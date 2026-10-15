package pe.edu.isil.pedidos.api;

import pe.edu.isil.pedidos.domain.Pedido;
import pe.edu.isil.pedidos.dto.*;
import pe.edu.isil.pedidos.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {
 private final PedidoRepository repository;
 public PedidoController(PedidoRepository repository) { this.repository=repository; }
 @PostMapping @ResponseStatus(HttpStatus.CREATED)
 public PedidoResponse crear(@RequestBody CrearPedidoRequest request) {
  if (request.productoCodigo()==null || request.productoCodigo().isBlank() || request.cantidad()<=0) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST,"Producto y cantidad positiva son obligatorios");
  return PedidoResponse.from(repository.save(new Pedido(request.productoCodigo(),request.cantidad())));
 }
 @GetMapping public List<PedidoResponse> listar() { return repository.findAll().stream().map(PedidoResponse::from).toList(); }
}