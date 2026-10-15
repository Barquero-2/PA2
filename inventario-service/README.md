# inventario-service

API Jakarta EE 10 desplegable como WAR en WildFly. Contexto esperado: `/inventario-service`; JAX-RS bajo `/api`.

## Configuración requerida

Crear un datasource JTA llamado `java:/jdbc/InventarioDS` conectado a SQL Server. Ejecutar/adaptar `sqlserver-schema.sql` solo tras confirmar que la tabla no existe. El driver JDBC debe estar instalado como módulo de WildFly.

## Endpoint

`POST /inventario-service/api/inventario/productos/codigo/P001/devoluciones`

```json
{"cantidad":2}
```

Respuesta `200`:
```json
{"codigo":"P001","cantidadDevuelta":2,"stockRestante":12}
```

Cantidad menor o igual a cero: `400`; código no existente: `404`. La operación se ejecuta dentro de la transacción del EJB y conserva el control optimista `@Version`.

## Pruebas

`mvn test` — pruebas unitarias de dominio sin WildFly ni SQL Server.
