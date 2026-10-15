package pe.edu.isil.pedidos.exception;
import org.springframework.http.HttpStatus;
public class ReglaNegocioException extends RuntimeException {
 private final HttpStatus status;
 public ReglaNegocioException(String message, HttpStatus status) { super(message); this.status=status; }
 public HttpStatus getStatus() { return status; }
}