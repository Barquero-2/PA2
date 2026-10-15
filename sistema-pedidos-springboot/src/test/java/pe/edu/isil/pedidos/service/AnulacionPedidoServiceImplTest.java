package pe.edu.isil.pedidos.service;

import pe.edu.isil.pedidos.client.*;
import pe.edu.isil.pedidos.domain.*;
import pe.edu.isil.pedidos.exception.ReglaNegocioException;
import pe.edu.isil.pedidos.repository.*;
import pe.edu.isil.pedidos.dto.DevolucionStockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnulacionPedidoServiceImplTest {
 private PedidoRepository pedidos; private AnulacionPedidoRepository anulaciones; private DevolucionStockClient inventario; private AnulacionPedidoServiceImpl service;
 @BeforeEach void setup() { pedidos=mock(PedidoRepository.class); anulaciones=mock(AnulacionPedidoRepository.class); inventario=mock(DevolucionStockClient.class); service=new AnulacionPedidoServiceImpl(pedidos,anulaciones,inventario); }
 @Test void anulacionCorrectaLlamaUnaVezMarcaYGuarda() {
  Pedido p=new Pedido("P001",2); setId(p,1L); when(pedidos.findById(1L)).thenReturn(Optional.of(p)); when(anulaciones.findByPedidoId(1L)).thenReturn(Optional.empty());
  when(inventario.devolver("P001",2)).thenReturn(new DevolucionStockResponse("P001",2,10));
  when(anulaciones.save(any())).thenAnswer(i->i.getArgument(0));
  AnulacionPedido result=service.anular(1L,"Solicitud del cliente");
  assertEquals(1L,result.getPedidoId()); assertEquals(PedidoEstado.ANULADO,p.getEstado()); verify(inventario,times(1)).devolver("P001",2); verify(anulaciones).save(any()); verify(pedidos).save(p);
 }
 @Test void pedidoYaAnuladoNoSolicitaSegundaDevolucion() {
  Pedido p=new Pedido("P001",2); setId(p,1L); p.anular(); when(pedidos.findById(1L)).thenReturn(Optional.of(p));
  assertThrows(ReglaNegocioException.class,()->service.anular(1L,"duplicado")); verifyNoInteractions(inventario); verify(anulaciones,never()).save(any());
 }
 @Test void falloRemotoNoGuardaAnulacionNiPedido() {
  Pedido p=new Pedido("P001",2); setId(p,1L); when(pedidos.findById(1L)).thenReturn(Optional.of(p)); when(anulaciones.findByPedidoId(1L)).thenReturn(Optional.empty());
  when(inventario.devolver("P001",2)).thenThrow(new InventarioClientException("down",new RuntimeException()));
  assertThrows(InventarioClientException.class,()->service.anular(1L,"test")); assertEquals(PedidoEstado.REGISTRADO,p.getEstado()); verify(anulaciones,never()).save(any()); verify(pedidos,never()).save(any());
 }
 @Test void pedidoInexistenteRetornaNotFoundSinLlamarInventario() {
  when(pedidos.findById(99L)).thenReturn(Optional.empty()); ReglaNegocioException ex=assertThrows(ReglaNegocioException.class,()->service.anular(99L,"x"));
  assertEquals(HttpStatus.NOT_FOUND,ex.getStatus()); verifyNoInteractions(inventario); verify(anulaciones,never()).save(any());
 }
 private static void setId(Pedido pedido, Long id) { try { var f=Pedido.class.getDeclaredField("id"); f.setAccessible(true); f.set(pedido,id); } catch (ReflectiveOperationException e) { throw new RuntimeException(e); } }
}