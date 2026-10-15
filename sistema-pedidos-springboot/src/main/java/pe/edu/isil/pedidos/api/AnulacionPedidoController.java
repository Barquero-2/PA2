package pe.edu.isil.pedidos.api;

import pe.edu.isil.pedidos.dto.*;
import pe.edu.isil.pedidos.service.AnulacionPedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/anulaciones")
public class AnulacionPedidoController {
 private final AnulacionPedidoService service;
 public AnulacionPedidoController(AnulacionPedidoService service) { this.service=service; }
 @PostMapping public ResponseEntity<AnulacionPedidoResponse> crear(@Valid @RequestBody CrearAnulacionRequest request) {
  return ResponseEntity.status(HttpStatus.CREATED).body(AnulacionPedidoResponse.from(service.anular(request.pedidoId(), request.motivo())));
 }
 @GetMapping public List<AnulacionPedidoResponse> listar() { return service.listar().stream().map(AnulacionPedidoResponse::from).toList(); }
}