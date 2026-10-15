# PA2 — Anulación de pedidos y devolución de stock

Solución de referencia organizada en dos proyectos independientes:

- `inventario-service`: Jakarta EE 10, JAX-RS, EJB, JPA y SQL Server; despliegue previsto en WildFly con contexto `/inventario-service` y puerto HTTP 8082.
- `sistema-pedidos-springboot`: Spring Boot 3, Spring MVC, Spring Data JPA, H2; puerto 8081 y contexto `/sistema-pedidos`.


## Requisitos

- JDK 21.
- Maven 3.9+.
- WildFly compatible con Jakarta EE 10 y SQL Server JDBC Driver para el proyecto de inventario.
- Para ejecutar inventario: configurar datasource JNDI `java:/jdbc/InventarioDS` y tabla `ProductoInventario` de acuerdo con `inventario-service/sqlserver-schema.sql`.
- Para pedidos: H2 se configura automáticamente; no requiere SQL Server.

## Pruebas

Desde cada carpeta, ejecutar `mvn test`. Las pruebas incluidas son unitarias y no reemplazan los cinco escenarios funcionales que se deben evidenciar con ambas aplicaciones ejecutándose.

## Ejecución local

1. Desplegar `inventario-service/target/inventario-service.war` en WildFly con datasource configurado.
2. Ejecutar `mvn spring-boot:run` dentro de `sistema-pedidos-springboot`.
3. Probar solicitudes de `pruebas-http/PA2.http` en VS Code REST Client o importar las mismas peticiones en Postman.
4. Antes de la prueba de indisponibilidad, detener WildFly; volver a iniciarlo para las demás pruebas.

## Contratos principales

- `POST http://localhost:8082/inventario-service/api/inventario/productos/codigo/{codigo}/devoluciones` body `{"cantidad":2}`.
- `POST http://localhost:8081/sistema-pedidos/api/v1/anulaciones` body `{"pedidoId":1,"motivo":"Solicitud del cliente"}`.
- `GET http://localhost:8081/sistema-pedidos/api/v1/anulaciones`.
- Pedidos de ejemplo: `POST /sistema-pedidos/api/v1/pedidos` con `{"productoCodigo":"P001","cantidad":2}`.

## Limitación distribuida

La transacción Spring solo cubre H2; no revierte una devolución ya realizada por HTTP. No hay reintentos automáticos de POST. El flujo secuencial impide duplicados con el estado y la restricción UNIQUE, pero no ofrece idempotencia distribuida ante concurrencia simultánea.
