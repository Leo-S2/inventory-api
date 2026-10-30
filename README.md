# Inventory API

API REST para gestionar el inventario de productos de una tienda: registrar productos, consultar su stock, actualizarlo y detectar los productos que se están agotando.

Proyecto personal de práctica con Spring Boot, construido siguiendo una arquitectura en capas (controller → service → repository).

## Tecnologías

- **Java 21**
- **Spring Boot 4** (Spring Web MVC, Spring Data JPA, Bean Validation)
- **PostgreSQL**
- **Lombok**
- **JUnit 5** y **Mockito** para tests unitarios
- **Gradle**

## Características

- CRUD de productos con validación de datos de entrada (`@Valid`).
- Uso de DTOs (`record`) para separar la API de las entidades JPA.
- Manejo centralizado de errores con `@RestControllerAdvice` y códigos HTTP adecuados (400, 404, 409).
- Nombres de producto únicos: crear un producto repetido devuelve **409 Conflict**.
- Transacciones de solo lectura por defecto (`@Transactional(readOnly = true)`) y transacciones de escritura en los métodos que modifican datos.
- Consultas derivadas de Spring Data (`existsByName`, `findByStockLessThanEqual`) para filtrar en la base de datos y no en memoria.

## Estructura del proyecto

```
src/main/java/com/leo/inventory_api
├── controller   → endpoints REST
├── service      → lógica de negocio
├── repository   → acceso a datos (Spring Data JPA)
├── model        → entidades JPA
├── dto          → objetos de entrada y salida de la API
└── exception    → excepciones propias y manejador global de errores
```

## Cómo ejecutarlo

### Requisitos

- Java 21
- PostgreSQL en ejecución en `localhost:5432`

### 1. Crear la base de datos

```sql
CREATE DATABASE inventory_db;
```

Las tablas se crean automáticamente al iniciar la aplicación.

### 2. Configurar la contraseña de la base de datos

La contraseña del usuario `postgres` se lee desde la variable de entorno `DB_PASSWORD`, para no guardarla en el código.

**Windows (PowerShell):**
```powershell
$env:DB_PASSWORD="tu_contraseña"
```

**Linux / macOS:**
```bash
export DB_PASSWORD=tu_contraseña
```

### 3. Iniciar la aplicación

**Windows:**
```powershell
.\gradlew.bat bootRun
```

**Linux / macOS:**
```bash
./gradlew bootRun
```

La API queda disponible en `http://localhost:8080`.

## Endpoints

Ruta base: `/api/products`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| `GET` | `/api/products` | Lista todos los productos | 200 |
| `GET` | `/api/products/{id}` | Obtiene un producto por su id | 200, 404 |
| `GET` | `/api/products/count` | Devuelve la cantidad total de productos | 200 |
| `GET` | `/api/products/low-stock?limit={n}` | Lista los productos con stock menor o igual a `n` | 200, 400 |
| `POST` | `/api/products` | Crea un producto | 201, 400, 409 |
| `PATCH` | `/api/products/{id}/stock` | Actualiza el stock de un producto | 204, 400, 404 |
| `DELETE` | `/api/products/{id}` | Elimina un producto | 204, 404 |

### Ejemplos

**Crear un producto**

```http
POST /api/products
Content-Type: application/json

{
  "name": "Teclado",
  "stock": 10,
  "price": 50.00
}
```

Respuesta `201 Created`:

```json
{
  "id": 1,
  "name": "Teclado",
  "price": 50.00,
  "stock": 10
}
```

**Actualizar el stock**

```http
PATCH /api/products/1/stock
Content-Type: application/json

{
  "stock": 25
}
```

Respuesta: `204 No Content`.

### Errores

| Código | Cuándo ocurre | Ejemplo de respuesta |
|---|---|---|
| `400` | Datos inválidos (nombre vacío, precio negativo o cero, stock negativo) | `{ "name": "must not be blank" }` |
| `404` | El producto no existe | `{ "message": "Product not found: 99" }` |
| `409` | Ya existe un producto con ese nombre | `{ "message": "Product with name Teclado already exists" }` |

## Tests

Los tests unitarios de `ProductService` usan Mockito para simular el repositorio, así que **no necesitan una base de datos**:

```bash
./gradlew test --tests "*ProductServiceTest"
```

Para ejecutar todos los tests (incluido el que levanta el contexto completo de Spring), PostgreSQL debe estar en ejecución y la variable `DB_PASSWORD` configurada:

```bash
./gradlew test
```

## Próximas mejoras

- Historial de movimientos de stock (entradas y salidas)
- Categorías de productos
- Paginación y búsqueda por nombre
- Migraciones con Flyway
- Docker Compose para PostgreSQL
- Documentación con Swagger / OpenAPI
