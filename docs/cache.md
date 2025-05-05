# Estrategia de Caché (Caffeine)

Para mejorar el rendimiento del sistema y reducir la carga sobre la base de datos, se implementa una caché en memoria utilizando **[Caffeine](https://github.com/ben-manes/caffeine/blob/master/README.md)**.

Esta estrategia permite:
- Evitar consultas repetitivas para la misma URL.
- Almacenar temporalmente objetos completos `ShortUrl`.
- Minimizar la latencia en redirecciones.

---

## ¿Qué se almacena?

Se decidió almacenar el objeto completo `ShortUrl` como valor en caché, usando el `shortCode` como clave.

Esto permite acceder a propiedades como:
- `longUrl`
- `isActive`
- `accessCount`
- `createdAt`

...sin necesidad de acceder a la base de datos.

---

## Entorno distribuido

En esta prueba técnica se usa **Caffeine** por ser simple, rápido y embebido en memoria.  
Pero en un entorno distribuido se recomienda reemplazarlo por **Redis** u otro caché centralizado para evitar inconsistencias entre instancias del backend.

---

## Propiedades configurables (`application.yml`)

Estas propiedades están bajo el prefijo `app.cache.short-url`:

| Propiedad             | Descripción                                                                | Valor por defecto |
|-----------------------|----------------------------------------------------------------------------|-------------------|
| `expire-after-write`  | Tiempo desde la última escritura tras el cual una entrada expira           | `10m`             |
| `expire-after-access` | *(Opcional)* Tiempo desde el último acceso tras el cual una entrada expira | — *(no definido)* |
| `maximum-size`        | Número máximo de entradas antes de aplicar política de expulsión           | `10000`           |
| `initial-capacity`    | Tamaño inicial de la caché para evitar redimensionamiento                  | `100`             |
| `record-stats`        | Habilita la recolección de estadísticas de uso                             | `false`           |

---

## Ejemplo de configuración

```yaml
app:
  cache:
    short-url:
      expire-after-write: 10m
      expire-after-access: 5m
      maximum-size: 10000
      initial-capacity: 100
      record-stats: false
```

## Observaciones
* La cache tiene un enfoque _**[write-through](https://www.geeksforgeeks.org/write-through-and-write-back-in-cache/)**_: al actualizar un ShortUrl, se persiste en la base y luego se actualiza la caché.
* No se elimina explícitamente el valor anterior, ya que `cache.put(key, value)` reemplaza la entrada si ya existe.

