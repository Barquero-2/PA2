package pe.edu.isil.pedidos.client;

import pe.edu.isil.pedidos.dto.DevolucionStockRequest;
import pe.edu.isil.pedidos.dto.DevolucionStockResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.ResourceAccessException;
import java.time.Duration;

@Component
public class DevolucionStockRestClient implements DevolucionStockClient {
    private final RestClient restClient;
    public DevolucionStockRestClient(@Value("${inventario.api.base-url}") String baseUrl,
        @Value("${inventario.api.connect-timeout-ms:1500}") int connectTimeoutMs,
        @Value("${inventario.api.read-timeout-ms:3000}") int readTimeoutMs) {
        var factory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        factory.setReadTimeout(Duration.ofMillis(readTimeoutMs));
        this.restClient = RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
    }
    @Override public DevolucionStockResponse devolver(String codigo, int cantidad) {
        try {
            return restClient.post().uri("/inventario/productos/codigo/{codigo}/devoluciones", codigo)
                .body(new DevolucionStockRequest(cantidad)).retrieve()
                .onStatus(HttpStatusCode::isError, (request,response) -> {
                    throw new InventarioClientException("Inventario respondió HTTP " + response.getStatusCode().value(), response.getStatusCode().value(), null);
                }).body(DevolucionStockResponse.class);
        } catch (InventarioClientException ex) { throw ex; }
          catch (RestClientResponseException ex) { throw new InventarioClientException("Error HTTP de inventario: " + ex.getStatusCode().value(), ex.getStatusCode().value(), ex); }
          catch (ResourceAccessException ex) { throw new InventarioClientException("Inventario no disponible o timeout", ex); }
    }
}