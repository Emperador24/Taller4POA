# Sistema de Gestión de Notas Académicas
## Taller 4 - Programación Orientada a Aspectos

### Estructura del Proyecto

```
Taller4POA/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/javeriana/gestionnotas/
│   │   │       ├── GestionNotasApplication.java
│   │   │       │
│   │   │       ├── model/
│   │   │       │   ├── Usuario.java
│   │   │       │   ├── Materia.java
│   │   │       │   └── Nota.java
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   ├── UsuarioRepository.java
│   │   │       │   ├── MateriaRepository.java
│   │   │       │   └── NotaRepository.java
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── UsuarioService.java
│   │   │       │   ├── MateriaService.java
│   │   │       │   └── NotaService.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── UsuarioController.java
│   │   │       │   ├── MateriaController.java
│   │   │       │   └── NotaController.java
│   │   │       │
│   │   │       ├── aspect/
│   │   │       │   ├── SecurityAspect.java
│   │   │       │   ├── ExceptionHandlerAspect.java
│   │   │       │   └── LoggingAspect.java
│   │   │       │
│   │   │       ├── config/
│   │   │       │   ├── SessionManager.java
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   └── DataInitializer.java
│   │   │       │
│   │   │       └── exception/
│   │   │           └── BusinessException.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html
│   │           └── app.js
│   │
│   └── test/
│       └── java/
│
├── pom.xml
├── README.md
└── InformeTecnico.md
```

### Requisitos Previos

- **Java 17 o superior**
- **Maven 3.6+**
- Navegador web moderno (Chrome, Firefox, Edge)


#### Paso 1: Compilar el proyecto

```bash
cd gestion-notas
mvn clean install
```

#### Paso 2: Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación se iniciará en `http://localhost:8080`

### Acceso a la Aplicación

1. **Interfaz Web**: http://localhost:8080/index.html
2. **API REST**: http://localhost:8080/api
3. **Consola H2**: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:notasdb`
   - Usuario: `sa`
   - Contraseña: (vacío)

### Usuarios de Prueba

La aplicación incluye datos iniciales:

| Rol | Email | Contraseña |
|-----|-------|-----------|
| Profesor | jorge.valenzuela@javeriana.edu.co | profesor123 |
| Alumno | juan.perez@javeriana.edu.co | alumno123 |
| Alumno | maria.garcia@javeriana.edu.co | alumno123 |

### API Endpoints

#### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/logout` - Cerrar sesión
- `GET /api/auth/session` - Obtener sesión actual

#### Usuarios
- `GET /api/usuarios` - Listar todos
- `GET /api/usuarios/{id}` - Obtener por ID
- `POST /api/usuarios` - Crear
- `PUT /api/usuarios/{id}` - Actualizar
- `DELETE /api/usuarios/{id}` - Eliminar

#### Materias
- `GET /api/materias` - Listar todas
- `GET /api/materias/{id}` - Obtener por ID
- `POST /api/materias` - Crear
- `PUT /api/materias/{id}` - Actualizar
- `DELETE /api/materias/{id}` - Eliminar

#### Notas
- `GET /api/notas` - Listar todas (filtradas por rol)
- `GET /api/notas/{id}` - Obtener por ID
- `POST /api/notas` - Crear (solo profesor)
- `PUT /api/notas/{id}` - Actualizar (solo profesor)
- `DELETE /api/notas/{id}` - Eliminar (solo profesor)
- `GET /api/notas/estudiante/{id}` - Notas de un estudiante
- `GET /api/notas/promedio/{id}` - Promedio de un estudiante
- `GET /api/notas/acumulada/{estudianteId}/{materiaId}` - Nota acumulada

### Características Principales

✅ **Programación Orientada a Aspectos (AOP)**
- Control de acceso automático basado en roles
- Logging transparente de operaciones
- Manejo centralizado de excepciones

✅ **Seguridad por Roles**
- **Alumno**: Solo lectura de sus propias notas
- **Profesor**: Acceso completo CRUD

✅ **Validaciones Automáticas**
- Porcentajes no superan 100%
- Notas en rango 0.0 - 5.0
- Emails y nombres de materia únicos

✅ **Cálculos Automáticos**
- Promedio ponderado por estudiante
- Nota acumulada por materia
- Valor ponderado por nota

### Tecnologías

- Spring Boot 3.2.0
- Spring Data JPA
- Spring AOP
- H2 Database
- Bean Validation
- Lombok
- Thymeleaf

### Troubleshooting

**Error: Puerto 8080 en uso**
```bash
# Cambiar puerto en application.properties
server.port=8081
```

**Error: Java version**
```bash
# Verificar versión de Java
java -version
# Debe ser Java 17 o superior
```

**Error de compilación**
```bash
# Limpiar y recompilar
mvn clean install -U
```

### Desarrollo

Para desarrollo con recarga automática:
```bash
mvn spring-boot:run
```

Los cambios en archivos estáticos (HTML, JS, CSS) se reflejan automáticamente con Spring DevTools.

### Testing

```bash
# Ejecutar tests
mvn test

# Con cobertura
mvn clean test jacoco:report
```

### Contacto y Soporte

Para preguntas sobre el proyecto, consultar la documentación de Spring Boot:
- https://spring.io/projects/spring-boot
- https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop

---

**Universidad Javeriana - Facultad de Ingeniería**
*Teoría de la Computación - Semestre 2025-30*