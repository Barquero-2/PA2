package pe.edu.isil.inventario.api;

import pe.edu.isil.inventario.dto.DevolucionStockRequest;
import pe.edu.isil.inventario.dto.DevolucionStockResponse;
import pe.edu.isil.inventario.service.InventarioService;
import pe.edu.isil.inventario.service.ProductoNoEncontradoException;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/inventario/productos/codigo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductoResource {
    @EJB private InventarioService inventarioService;

    @POST @Path("{codigo}/devoluciones")
    public Response devolver(@PathParam("codigo") String codigo, DevolucionStockRequest request) {
        if (request == null || request.getCantidad() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("CANTIDAD_INVALIDA", "La cantidad debe ser mayor que cero")).build();
        }
        try {
            DevolucionStockResponse result = inventarioService.devolver(codigo, request.getCantidad());
            return Response.ok(result).build();
        } catch (ProductoNoEncontradoException ex) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("PRODUCTO_NO_ENCONTRADO", ex.getMessage())).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("SOLICITUD_INVALIDA", ex.getMessage())).build();
        }
    }
    public static class ErrorResponse {
        private String codigo; private String mensaje;
        public ErrorResponse() { }
        public ErrorResponse(String codigo, String mensaje) { this.codigo=codigo; this.mensaje=mensaje; }
        public String getCodigo() { return codigo; } public String getMensaje() { return mensaje; }
    }
}