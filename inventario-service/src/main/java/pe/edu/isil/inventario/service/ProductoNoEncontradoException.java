package pe.edu.isil.inventario.service;

public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException(String message) { super(message); }
}