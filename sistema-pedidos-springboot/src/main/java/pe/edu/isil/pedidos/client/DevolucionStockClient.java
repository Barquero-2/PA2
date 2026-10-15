package pe.edu.isil.pedidos.client;
import pe.edu.isil.pedidos.dto.DevolucionStockResponse;
public interface DevolucionStockClient {
    DevolucionStockResponse devolver(String codigo, int cantidad);
}