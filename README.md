# Pastelería Mil Sabores - Backend

## Evaluación Parcial 3

Desarrollo Fullstack II - DSY1104
Duoc UC

## Descripción

API REST para la gestión de una tienda online de pastelería,
integrada con un frontend desarrollado en React.

El sistema permite administrar usuarios, productos, categorías,
compras y boletas mediante una arquitectura por capas.

## Tecnologías

- Java 17
- Spring Boot 4.0.9
- Spring Data JPA
- Spring Security
- JWT
- BCrypt
- MySQL 8
- Maven
- Swagger / OpenAPI
- JUnit
- H2

## Arquitectura

- Controller: exposición de endpoints REST.
- Service: reglas de negocio.
- Repository: acceso a datos.
- Entity: persistencia.
- DTO: transferencia y validación de datos.
- Security: autenticación y autorización.
- Exception: gestión de errores.

## Instalación

### 1. Requisitos

- Java JDK 17.
- MySQL Server 8.
- Git.
- MySQL Workbench (opcional).

### 2. Clonar repositorio

```bash
git clone https://github.com/kodevlyu/milsabores-backend.git
cd milsabores-backend
```

### 3. Configurar MySQL

En una base de datos nueva, ejecutar los archivos en orden:

1. database/schema.sql
2. database/data.sql
3. database/migracion_catalogo_ep2.sql

El esquema incluye la tabla refresh_token.

La migración migracion_refresh_token.sql se conserva
para instalaciones anteriores.

No ejecutar nuevamente los datos iniciales sobre una
base de datos que contiene información existente.

### 4. Configurar variables de entorno

Ejecutar en PowerShell:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/milsabores"
$env:DB_USER="root"
$env:DB_PASSWORD=[System.Net.NetworkCredential]::new("", (Read-Host "Contrasena MySQL" -AsSecureString)).Password
$env:SPRING_PROFILES_ACTIVE="local"

$bytes=New-Object byte[] 32
$rng=[System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
$env:JWT_SECRET=[Convert]::ToBase64String($bytes)
$rng.Dispose()

$env:JWT_EXPIRATION="900000"
```

No publicar credenciales de MySQL ni claves JWT.

Las variables deben configurarse en la terminal
desde la cual se ejecutará el backend.

### 5. Iniciar backend

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

## URLs

- Backend: http://localhost:8080
- API REST: http://localhost:8080/api/v1
- Swagger: http://localhost:8080/swagger-ui/index.html
- OpenAPI: http://localhost:8080/v3/api-docs
- Frontend: http://localhost:5173

## Cuentas de demostración

| Rol | Correo |
|---|---|
| ADMIN | admin@example.com |
| VENDEDOR | vendedor@example.com |
| CLIENTE | cliente@example.com |

Contraseña de demostración: MilSaboresDemo2026!

Estas cuentas corresponden exclusivamente a instalaciones
académicas nuevas realizadas con data.sql.

Utilizar únicamente en un entorno local de demostración.
No utilizar estas credenciales en producción.

## Módulos

- Autenticación.
- Productos.
- Categorías.
- Usuarios.
- Boletas.
- Diagnóstico.

## Seguridad

- Autenticación mediante JWT.
- Contraseñas almacenadas mediante BCrypt.
- Autorización por roles.
- Renovación y rotación de refresh tokens.
- Revocación de refresh tokens al cerrar sesión.
- Configuración CORS.
- Validación de solicitudes.
- Gestión de errores HTTP.

## Modelo de datos

- usuario
- categoria
- producto
- boleta
- detalle_boleta
- refresh_token

## Pruebas automatizadas

```powershell
.\mvnw.cmd clean test
```

Las pruebas utilizan H2 en memoria, independiente
de la base de datos MySQL de desarrollo.

## Frontend

Repositorio:

https://github.com/kodevlyu/fullstack-development-2-ev2

Rama correspondiente a la EP3:

feature/integracion-backend-ep3

## Documentación

- Especificación de requisitos de software.
- Anexo 2: documentación de APIs e integración.
- Anexo 3: documentación de APIs.
- Anexo 4: manual de usuario.
