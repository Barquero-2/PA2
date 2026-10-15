# sistema-pedidos-springboot

Spring Boot 3, Spring MVC, Spring Data JPA y H2. Puerto `8081`, contexto `/sistema-pedidos`.

## Arranque

```bash
mvn test
mvn spring-boot:run
```

URL base del inventario centralizada en `src/main/resources/application.properties`. El cliente usa `RestClient`, DTOs y constructor injection; no reintenta automáticamente solicitudes POST.

## Crear pedido de prueba

`POST http://localhost:8081/sistema-pedidos/api/v1/pedidos`
```json
{"productoCodigo":"P001","cantidad":2}
```

## Anular

`POST http://localhost:8081/sistema-pedidos/api/v1/anulaciones`
```json
{"pedidoId":1,"motivo":"Solicitud del cliente"}
```

Respuesta exitosa: `201 Created`. Un pedido inexistente devuelve `404`, duplicado `409`, y fallo del inventario `502 Bad Gateway`. Consultar anulaciones con `GET /sistema-pedidos/api/v1/anulaciones`.

La transacción local cubre H2 únicamente; el POST remoto no se revierte automáticamente si falla una escritura local posterior. No hay reintentos automáticos.
