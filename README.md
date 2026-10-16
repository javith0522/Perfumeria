# 🌸 NOIR - Sistema Integral de E-commerce y Gestión de Perfumería

Backend robusto desarrollado en **Spring Boot** diseñado para operar tanto el flujo de venta en línea (catálogo, carrito, pasarela de pagos y autenticación biométrica) como la administración interna del negocio (inventario estricto, trazabilidad de movimientos y control financiero).

---

## 🏗️ Arquitectura y Módulos del Sistema

El sistema está dividido en los siguientes subsistemas principales:

### 1. 📦 Módulo de Inventario y Existencias
- **Gestión de Productos (RF-1):** Registro, edición, control de presentación en mililitros y estados (activo/inactivo).
- **Trazabilidad de Movimientos (RNF-4):** Bitácora inalterable de entradas, salidas por daño/uso y ajustes de stock.
- **Control de Stock y Alertas (RF-11):** Evaluación dinámica del estado del producto mediante estados algorítmicos (`AGOTADO`, `BAJO`, `NORMAL`, `INACTIVO`).
- **Descuento Automático (RF-6):** Actualización inmediata del stock al concretarse una venta u orden.

### 2. 🛒 Módulo de E-commerce y Carrito
- Gestión de ítems en el carrito de compras por usuario.
- Validación de disponibilidad de inventario en tiempo real antes de procesar la compra.

### 3. 💳 Módulo de Órdenes y Pagos
- Procesamiento de órdenes de compra y detalle de artículos.
- Integración con pasarela de pagos **Wompi** (`WompiController`).

### 4. 🔒 Seguridad y Autenticación
- **Control de Acceso (RF-15 / RNF-1):** Protección de rutas administrativas mediante **Spring Security** y contraseñas cifradas.
- **Autenticación Biométrica:** Soporte para validaciones de identidad avanzadas (`BiometriaController`).

---

## 🛠️ Tecnologías y Stack Tecnológico
- **Lenguaje:** Java 17+
- **Framework Principal:** Spring Boot (Web, Data JPA, Security)
- **Base de Datos:** MySQL / PostgreSQL con Hibernate ORM
- **Gestión de Dependencias:** Maven
- **Herramientas de Utilidad:** Lombok

---

## Guía de Instalación y Ejecución Local

### Prerrequisitos
- Tener instalado **Java 17** o superior.
- Tener instalado **Maven** y un manejador de base de datos (MySQL o PostgreSQL).

### Pasos para levantar el proyecto:

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/javith0522/Perfumeria.git](https://github.com/javith0522/Perfumeria.git)