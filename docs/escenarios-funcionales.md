# Registro de ejecución — completar con evidencias reales

| Caso | Preparación | Solicitud | Resultado esperado | Evidencia pendiente |
|---|---|---|---|---|
| 1. Anulación correcta | Pedido REGISTRADO de 2 unidades; registrar stock inicial | POST `/api/v1/anulaciones` | 201, stock +2, estado ANULADO, una anulación | Captura Postman + consulta H2 + stock SQL |
| 2. Duplicada | Usar el mismo pedido ya ANULADO | Repetir POST | 409; stock no cambia por segunda vez | Captura respuesta + stock + conteo anulaciones |
| 3. Pedido inexistente | ID que no exista | POST anulación | 404; sin cambios de stock ni registro | Captura respuesta + consultas |
| 4. Inventario detenido | Pedido REGISTRADO; detener WildFly | POST anulación | 502; pedido REGISTRADO; sin anulación | Captura respuesta + H2 |
| 5. Contrato inventario | Anotar stock de P001 | POST cantidades 2, 0, -1 y código inexistente | 200 para válida, 400 para inválidas, 404 inexistente | Capturas y stock antes/después |

No marcar como completado hasta ejecutar cada caso en las aplicaciones desplegadas.
