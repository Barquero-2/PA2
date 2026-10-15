# Informe PA2: Integración de anulación de pedidos y devolución de stock

Curso: Desarrollo de Aplicaciones Empresariales Avanzado  
NRC:4622
Docente:Carlos Augusto Nole Machaca 
Estudiante:Carlos Mathias Barquero Jara
Periodo:2026-2

## 1. Objetivo

Ampliar la integración entre el sistema de pedidos y el inventario mediante un contrato REST de devolución de stock. La solución separa responsabilidades por capas y aplica inyección de dependencias, DTOs y persistencia independiente para cada aplicación.

## 2. Arquitectura y responsabilidades

`inventario-service` expone `POST /api/inventario/productos/codigo/{codigo}/devoluciones`. `ProductoResource` valida el contrato HTTP y traduce errores a códigos HTTP; `InventarioService` ejecuta la lógica transaccional con EJB; `ProductoInventario` mantiene la regla de dominio y el control optimista `@Version`; JPA persiste en SQL Server.

`sistema-pedidos-springboot` recibe la anulación en `AnulacionPedidoController`, delega en `AnulacionPedidoServiceImpl`, y persiste con `PedidoRepository` y `AnulacionPedidoRepository` en H2. `DevolucionStockClient` desacopla la lógica de negocio del transporte HTTP y `DevolucionStockRestClient` implementa el contrato con `RestClient`. Los DTOs no exponen entidades JPA.

Insertar aquí la imagen/exportación del diagrama de `arquitectura.mmd` o convertirlo con Mermaid compatible.

## 3. Reglas de negocio y manejo de errores

- La devolución solo acepta cantidades positivas; cantidad inválida responde `400`, código de producto inexistente responde `404`.
- Solo se anulan pedidos existentes en estado `REGISTRADO`; un intento repetido responde `409`.
- La llamada remota se realiza antes de cambiar y guardar el estado local. Si el inventario no responde, se propaga un error técnico controlado (`502`) y no se registra una anulación exitosa.
- La entidad de anulación tiene restricción única sobre `pedidoId`; el pedido se conserva y su estado cambia a `ANULADO`.

## 4. Pruebas

Pruebas unitarias nuevas:

1. `ProductoInventarioTest.devolucionValidaIncrementaStockExactamente`: verifica 8 + 2 = 10.
2. `ProductoInventarioTest.cantidadCeroONegativaSeRechazaSinCambiarStock`: verifica cantidades 0 y negativas.
3. `AnulacionPedidoServiceImplTest.anulacionCorrectaLlamaUnaVezMarcaYGuarda`: valida llamada única, estado y persistencia.
4. `AnulacionPedidoServiceImplTest.pedidoYaAnuladoNoSolicitaSegundaDevolucion`: evita segunda llamada remota y segundo registro.
5. `AnulacionPedidoServiceImplTest.falloRemotoNoGuardaAnulacionNiPedido`: comprueba que el fallo remoto no confirma cambios locales.
6. `AnulacionPedidoServiceImplTest.pedidoInexistenteRetornaNotFoundSinLlamarInventario`: verifica 404 y ausencia de llamada remota.

Comandos previstos: `mvn test` desde cada proyecto. **Pendiente ejecutar y adjuntar resultados reales**: el entorno de preparación no dispone de Maven ni de acceso de red para descargar dependencias.

## 5. Cinco pruebas funcionales requeridas

1. **Anulación correcta:** crear pedido de dos unidades; anotar stock inicial; anular; comprobar stock aumenta en dos, el pedido queda `ANULADO` y aparece el registro.
2. **Duplicada:** repetir el POST; comprobar `409`, stock sin cambio adicional y un solo registro.
3. **Pedido inexistente:** usar un ID no registrado; comprobar `404`, inventario intacto y cero registros nuevos.
4. **Inventario indisponible:** detener WildFly e intentar anular; comprobar `502`, pedido `REGISTRADO` y ausencia de anulación confirmada. Volver a iniciar WildFly después.
5. **Contrato de inventario:** enviar cantidad válida, cero y negativa, y código inexistente; registrar JSON y HTTP status, y verificar stock solo cambia en el caso válido.


## 6. Inyección de dependencias y transacciones

Jakarta EE utiliza `@EJB` para inyectar el servicio EJB administrado por el contenedor; el contenedor aplica servicios como la transacción local del EJB. Spring utiliza constructor injection para `AnulacionPedidoServiceImpl` y el controller, facilitando pruebas con Mockito y dejando explícitas las dependencias.

`@Transactional` administra el estado local de H2, no una transacción distribuida que incluya SQL Server a través de HTTP. Por ello, una devolución remota puede confirmarse aunque una operación local posterior falle. No se reintenta automáticamente el POST, ya que un timeout puede ocultar una respuesta exitosa y causar devolución duplicada.


