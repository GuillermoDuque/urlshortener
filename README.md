# URL Shortener

Este proyecto es un acortador de URLs diseñado para publicar promociones en redes sociales como Twitter.

> ⚠️ Esta es una prueba técnica: se logró una simulación estable de **~5040 RPM (≈84 RPS)** utilizando K6 con 100% de éxito. Aunque no alcanza 1 millón de RPM reales, la arquitectura propuesta está pensada para escalar progresivamente.

El objetivo es demostrar buenas prácticas de desarrollo backend, incluyendo:
- Código limpio y principios SOLID
- Pruebas automatizadas
- Separación de responsabilidades
- Tolerancia a fallos y diseño escalable
- Configuración externa via YAML

---

## Índice

- [Endpoints](#Endpoints)
- [Estrategia de Caché](docs/cache.md)
- [Base de Datos](docs/database.md)
- [Testing y Cobertura](docs/testing.md)
- [Buenas Prácticas y Patrones](docs/best-practices.md)
- [Arquitectura del Proyecto y Escalamiento Productivo](docs/architecture.md)
- [Cómo levantar el proyecto](#cómo-levantar-el-proyecto)
- [Supuestos realizados](#supuestos-realizados)


---

## Endpoints

| Método | Endpoint               | Descripción                                                                  |
|--------|------------------------|------------------------------------------------------------------------------|
| POST   | `/shorten`             | Crea una nueva URL corta a partir de una URL larga. Retorna la URL generada. |
| GET    | `/{shortCode}`         | Redirige a la URL original si el código corto es válido y está activo.       |
| PUT    | `/shorten/{shortCode}` | Actualiza la URL larga o el estado activo/inactivo de un código existente.   |
| GET    | `/{shortCode}/stats`   | Retorna estadísticas de acceso (visitas, estado, fecha de creación).         |
---

## Cómo levantar el proyecto

Asegúrate de tener Docker y Docker Compose instalados.

```bash
docker compose up --build -d
```

Esto levantará:
* La base de datos PostgreSQL(puerto 5433)
* El backend (8080) con el profile postgres

## Apagar servicios

```bash
docker compose down
```

---

## Demo pública (Render)

Una versión funcional del servicio se encuentra desplegada en Render:

🔗 **https://urlshortener-a9ft.onrender.com**

Puedes probarla directamente desde herramientas como `curl`, Postman o desde el navegador.  
Por ejemplo, para acortar una URL:

```bash
curl -X POST https://urlshortener-a9ft.onrender.com/shorten \
  -H "Content-Type: application/json" \
  -d '{"longUrl":"https://example.com"}'

```

> Por inactividad la aplicación queda suspendida, así que ideal hacer una petición previa.

## Supuestos realizados
* Las URL se almacenan indefinidamente, salvo modificación.
* No se requiere autenticación para uso básico del API.
* El sistema garantiza idempotencia: si una URL larga ya fue acortada previamente, se retorna siempre el mismo código corto