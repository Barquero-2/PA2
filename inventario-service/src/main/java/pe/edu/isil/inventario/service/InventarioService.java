package pe.edu.isil.inventario.service;

import pe.edu.isil.inventario.domain.ProductoInventario;
import pe.edu.isil.inventario.dto.DevolucionStockResponse;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;

@Stateless
public class InventarioService {
    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public DevolucionStockResponse devolver(String codigo, int cantidad) {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("El código es obligatorio");
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad devuelta debe ser mayor que cero");
        try {
            ProductoInventario producto = em.createQuery(
                "SELECT p FROM ProductoInventario p WHERE p.codigo = :codigo", ProductoInventario.class)
                .setParameter("codigo", codigo).getSingleResult();
            int stock = producto.devolverStock(cantidad);
            em.flush();
            return new DevolucionStockResponse(producto.getCodigo(), cantidad, stock);
        } catch (NoResultException ex) {
            throw new ProductoNoEncontradoException("No existe el producto con código " + codigo);
        }
    }
}