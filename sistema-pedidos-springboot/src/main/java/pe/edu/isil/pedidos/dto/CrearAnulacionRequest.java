package pe.edu.isil.pedidos.dto;
import jakarta.validation.constraints.NotNull;
public record CrearAnulacionRequest(@NotNull Long pedidoId, String motivo) { }