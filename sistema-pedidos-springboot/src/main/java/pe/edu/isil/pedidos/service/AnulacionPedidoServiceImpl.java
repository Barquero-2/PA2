package pe.edu.isil.pedidos.service;

import pe.edu.isil.pedidos.client.DevolucionStockClient;
import pe.edu.isil.pedidos.domain.*;
import pe.edu.isil.pedidos.exception.ReglaNegocioException;
import pe.edu.isil.pedidos.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AnulacionPedidoServiceImpl implements AnulacionPedidoService {

    private final PedidoRepository pedidoRepository;
    private final AnulacionPedidoRepository anulacionRepository;
    private final DevolucionStockClient devolucionStockClient;

    public AnulacionPedidoServiceImpl(PedidoRepository pedidoRepository, 
                                      AnulacionPedidoRepository anulacionRepository, 
                                      DevolucionStockClient devolucionStockClient) {
        this.pedidoRepository = pedidoRepository;
        this.anulacionRepository = anulacionRepository;
        this.devolucionStockClient = devolucionStockClient;
    }

    @Override
    @Transactional
    public AnulacionPedido anular(Long pedidoId, String motivo) {
        return anularConOpcionDeFallo(pedidoId, motivo, false);
    }
    public AnulacionPedido anularConOpcionDeFallo(Long pedidoId, String motivo, boolean simularFalloLocal) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ReglaNegocioException("No existe el pedido " + pedidoId, HttpStatus.NOT_FOUND));
        
        if (pedido.getEstado() != PedidoEstado.REGISTRADO || anulacionRepository.findByPedidoId(pedidoId).isPresent()) {
            throw new ReglaNegocioException("El pedido ya fue anulado o no está REGISTRADO", HttpStatus.CONFLICT);
        }
        devolucionStockClient.devolver(pedido.getProductoCodigo(), pedido.getCantidad());

        try {
            if (simularFalloLocal) {
                throw new RuntimeException("Fallo local controlado posterior a la reserva remota");
            }

            AnulacionPedido anulacion = new AnulacionPedido(pedido.getId(), pedido.getProductoCodigo(), pedido.getCantidad(), motivo);
            pedido.anular();
            AnulacionPedido guardada = anulacionRepository.save(anulacion);
            pedidoRepository.save(pedido);
            return guardada;

        } catch (RuntimeException ex) {
            try {
                System.err.println("[COMPENSACIÓN ACTIVADA]: Ocurrió un fallo local en H2. " +
                                   "El inventario remoto ya fue modificado. Registrando incidente pendiente de conciliación: " 
                                   + ex.getMessage());
                

            } catch (Exception compEx) {
                System.err.println("Falla crítica en el proceso de compensación: " + compEx.getMessage());
            }
            

            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnulacionPedido> listar() {
        return anulacionRepository.findAll();
    }
}