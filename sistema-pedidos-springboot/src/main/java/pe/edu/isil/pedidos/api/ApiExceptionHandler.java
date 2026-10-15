package pe.edu.isil.pedidos.api;

import pe.edu.isil.pedidos.client.InventarioClientException;
import pe.edu.isil.pedidos.exception.ReglaNegocioException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(ReglaNegocioException.class)
 public ResponseEntity<Map<String,Object>> negocio(ReglaNegocioException ex) { return ResponseEntity.status(ex.getStatus()).body(Map.of("timestamp", Instant.now().toString(),"error",ex.getStatus().getReasonPhrase(),"mensaje",ex.getMessage())); }
 @ExceptionHandler(InventarioClientException.class)
 public ResponseEntity<Map<String,Object>> inventario(InventarioClientException ex) { return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("timestamp",Instant.now().toString(),"error","INVENTARIO_NO_DISPONIBLE","mensaje",ex.getMessage())); }
 @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
 public ResponseEntity<Map<String,Object>> duplicado(Exception ex) { return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","CONFLICT","mensaje","Ya existe una anulación para este pedido")); }
}