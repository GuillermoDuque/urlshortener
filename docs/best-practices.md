# Buenas Prácticas Aplicadas

Este proyecto aplica varias buenas prácticas de diseño, arquitectura y desarrollo backend moderno. A continuación se describen los enfoques más destacados:

---

## Estructura por capas y responsabilidad única

El proyecto está dividido en paquetes que reflejan una separación clara de responsabilidades, siguiendo una estructura inspirada en DDD:

| Carpeta          | Rol principal                                                    |
|------------------|------------------------------------------------------------------|
| `application`    | Lógica orquestadora (servicios que coordinan flujo del negocio). |
| `domain`         | Modelo de dominio, interfaces y contratos.                       |
| `infrastructure` | Implementaciones técnicas (repositorios, cache, generadores).    |
| `presentation`   | Controladores, DTOs y manejo de errores HTTP.                    |
| `config`         | Configuración y beans personalizados.                            |

---

## Testing organizado

- **Unit tests** para lógica de dominio e infraestructura.
- **Integration tests** para flujos de extremo a extremo con MockMvc.
- Clase de soporte `BaseTestSupport` para reutilizar datos de prueba comunes.

---

## Inyección de dependencias explícita

Se utiliza inyección por constructor en lugar de `@Autowired`, lo que:

- Favorece la claridad y la inmutabilidad.
- Facilita la escritura de pruebas unitarias.
- Elimina la dependencia de frameworks en clases del dominio.

---

## Uso de DTOs

La capa de presentación define clases específicas para representar los datos de entrada y salida:

- `ShortenUrlRequest`, `ShortenUrlUpdateRequest` para recibir datos.
- `ShortenUrlResponse` para retornar datos al cliente.

Esto evita acoplar el modelo de dominio con el contrato de la API.

---

## Configuración externa

- Parámetros como límites de caché o URL base se definen en `application.yml`.
- Se usan clases `@ConfigurationProperties` (`AppProperties`, `CacheProperties`) para mapear automáticamente la configuración.

---

## Cache desacoplado

- Se implementa un cache en memoria con [Caffeine](https://github.com/ben-manes/caffeine).
- La clase `ShortUrlCache` abstrae el uso interno del cache.
- Se aplica una estrategia **write-through** para mantener sincronización entre cache y base de datos.

---

## Manejo de errores centralizado

- `GlobalExceptionHandler` captura excepciones controladas y devuelve errores uniformes.
- `ApiError` permite representar errores con mensaje, código y detalles en el cuerpo de la respuesta.

---

## Generación de short codes modular

- Se define la interfaz `ShortCodeGenerator` para abstraer el algoritmo.
- La implementación actual `Base62ShortCodeGenerator` puede reemplazarse fácilmente sin modificar la lógica de negocio implementando ShortCodeGenerator.

---

## Código limpio y mantenible

- Uso consistente de `logger` para trazabilidad.
- Separación clara de responsabilidades en clases.
- Métodos cortos y con nombres expresivos.
- Documentación técnica en el código fuente y en archivos `.md`.