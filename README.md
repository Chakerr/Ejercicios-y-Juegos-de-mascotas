# Wagglo - Aplicación para el Cuidado de Mascotas

**Proyecto:** Wagglo (Aplicación para Ejercicios y Juegos de Mascotas)  
**Plataformas:** Android (Wagglo) + Spring Boot (SpringServer) + PostgreSQL


## Aplicación Móvil - Wagglo


Wagglo es una app Android desarrollada para que paseadores y dueños de mascotas gestionen el bienestar de sus mascotas. A través de ella, se pueden registrar datos médicos, realizar seguimientos de actividad física y más.

### Funcionalidades principales:

- **Inicio de sesión** y gestión de usuarios (paseadores y dueños).
- **Registro y visualización de mascotas**, con historial médico.
- **Creación y visualización de rutas de paseo**, integrando mapas interactivos.
- **Simulación e historial de recorridos.**
- **Notificaciones** automáticas para los dueños cuando inicia/finaliza un paseo.
- **Selección de mascotas** para paseo mediante un spinner.
- **Almacenamiento local de sesión** con SharedPreferences.
- **Integración con Retrofit** para la conexión al backend.

---

## Backend - Spring Boot (SpringServer)

API REST desarrollada con Spring Boot que gestiona usuarios, mascotas, rutas y notificaciones.

### Componentes clave:

- **Controladores REST** para usuarios, mascotas y rutas.
- **Servicios** de negocio para lógica personalizada.
- **Repositorio con Spring Data JPA** (PostgreSQL).
- **Entidades:** Usuario, Mascota, Historial Médico, Ruta, Punto Geográfico.
- **Persistencia de rutas geográficas** con `@ElementCollection`.
- **Endpoints REST para registrar y consultar rutas**, con y sin notificaciones.
- **Notificación por recorrido (inicio/fin)** usando booleanos notificadoInicio / notificadoFin.

---

## Estructura del Proyecto

```
Wagglo/
├── app/
│   ├── src/
│   │   └── main/java/co/edu/unipiloto/petapp/
│   │       ├── model/        # Modelos de datos: Mascota, Usuario, RutaMascota...
│   │       ├── retrofit/     # Servicios y cliente Retrofit
│   │       ├── workers/      # Worker para notificaciones automáticas
│   │       ├── activities/   # Actividades de Android (UI)
│   │       └── ...           # Otros componentes (Login, Menús, etc.)
│   └── res/layout/           # Layouts XML
│
SpringServer/
├── src/main/java/com/Mario/SpringServer/
│   ├── controller/           # Controladores REST
│   ├── service/              # Lógica de negocio
│   ├── model/                # Entidades JPA
│   ├── repository/           # Repositorios JPA
│   └── ...                   # Configuraciones y aplicación
```

---
