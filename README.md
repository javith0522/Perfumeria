# 🌸 NOIR · E-commerce y Gestión de Perfumería

Backend desarrollado con **Spring Boot** que cubre dos frentes: la venta en línea (catálogo, carrito, órdenes y pagos con Wompi) y la administración interna del negocio (inventario con trazabilidad, gastos, proveedores y auditoría).

> 🚧 **Proyecto en desarrollo.** Los módulos de catálogo, carrito, órdenes, pagos y seguridad están implementados. El inventario avanzado, los reportes y algunas funciones del asistente de voz siguen en construcción.

---

## 🏗️ Módulos

### 📦 Inventario y existencias
- Registro y edición de productos, con presentación en mililitros y estado (activo/inactivo).
- **Trazabilidad de movimientos:** bitácora de entradas, salidas por daño o uso y ajustes de stock.
- **Estados de stock calculados:** `AGOTADO`, `BAJO`, `NORMAL` e `INACTIVO`.
- **Descuento automático** del stock al concretar una venta.

### 🛒 Carrito y órdenes
- Carrito de compras por usuario.
- Validación de disponibilidad antes de procesar la compra.
- Órdenes con su detalle de artículos.

### 💳 Pagos
- Integración con la pasarela **Wompi**.

### 🔒 Seguridad
- **Spring Security** con rutas administrativas protegidas y contraseñas cifradas con BCrypt.
- Soporte para validación biométrica (`BiometriaController`).

### 🧾 Administración
- Gastos por categoría, proveedores y registro de auditoría.

### 🎙️ Asistente de voz
- Endpoint de texto a voz con **ElevenLabs**.

---

## 🛠️ Tecnologías

| Área | Herramienta |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot (Web, Data JPA, Security) |
| Base de datos | MySQL 8 con Hibernate |
| Build | Maven |
| Contenedores | Docker y Docker Compose |
| Pagos | Wompi |
| Voz | ElevenLabs |
| Utilidades | Lombok |

---

## 🚀 Ejecución con Docker (recomendada)

**Requisitos:** Docker Desktop (o Docker y Docker Compose).

1. Clona el repositorio:
```bash
   git clone https://github.com/javith0522/Perfumeria.git
   cd Perfumeria
```

2. Crea tu archivo de variables a partir del ejemplo:
```bash
   cp .env.example .env
```
En Windows (PowerShell): `copy .env.example .env`

3. Abre `.env` y completa tus valores (ver la tabla de abajo).

4. Levanta la base de datos y la aplicación:
```bash
   docker-compose up --build
```

5. La API queda disponible en **http://localhost:8081**.

Para detenerlo: `docker-compose down`.

### Variables de entorno

| Variable | Descripción |
|---|---|
| `MYSQL_ROOT_PASSWORD` | Contraseña de la base de datos MySQL |
| `NOIR_ADMIN_USERNAME` | Usuario del administrador |
| `NOIR_ADMIN_PASSWORD` | Contraseña del administrador |
| `WOMPI_LLAVE_PRIVADA` | Llave privada de Wompi |
| `WOMPI_API_URL` | URL de la API de Wompi (usa `https://sandbox.wompi.co/v1` para pruebas) |
| `ELEVENLABS_API_KEY` | Clave de ElevenLabs para la voz del asistente |

> ⚠️ **Nunca subas tu archivo `.env`.** Está incluido en `.gitignore`. Para pruebas usa siempre las llaves de **sandbox** de Wompi.

---

## 💻 Ejecución local (sin Docker)

**Requisitos:** Java 17, Maven y MySQL 8.

1. Crea la base de datos:
```sql
   CREATE DATABASE perfumeria;
```

2. Define las variables de entorno de la tabla anterior, más las de conexión:
```
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/perfumeria?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=tu_clave
```

3. Ejecuta:
```bash
   ./mvnw spring-boot:run
```
En Windows: `mvnw.cmd spring-boot:run`

Las tablas se crean automáticamente en el primer arranque (`ddl-auto=update`).

---

## 🔐 Acceso de administrador

Las rutas `/api/admin/**` exigen autenticación con el usuario y la contraseña definidos en `NOIR_ADMIN_USERNAME` y `NOIR_ADMIN_PASSWORD` (autenticación HTTP Basic).

---

## 📁 Estructura del proyecto

```
src/main/java/perfume/Perfumeria/
 ├── controller    Endpoints REST (incluye Wompi y biometría)
 ├── service       Lógica de negocio
 ├── repository    Acceso a datos (Spring Data JPA)
 ├── model         Entidades
 ├── Dtos          Objetos de transferencia
 └── enums         Estados, tipos de movimiento y categorías
```

---

## 🗺️ Próximos pasos

- [ ] Reportes de ventas y de inventario
- [ ] Alertas de stock bajo para el administrador
- [ ] Pruebas unitarias de los servicios de inventario


---

## 👤 Autor

**Javith** · [GitHub](https://github.com/javith0522)