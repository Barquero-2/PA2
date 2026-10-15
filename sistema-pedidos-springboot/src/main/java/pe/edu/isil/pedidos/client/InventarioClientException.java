package pe.edu.isil.pedidos.client;
public class InventarioClientException extends RuntimeException {
 private final int status;
 public InventarioClientException(String message, int status, Throwable cause) { super(message,cause); this.status=status; }
 public InventarioClientException(String message, Throwable cause) { this(message, 0, cause); }
 public int getStatus() { return status; }
}