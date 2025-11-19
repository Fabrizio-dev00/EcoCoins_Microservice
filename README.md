# 🌱 EcoCoins Campus - Backend Microservice

> Sistema de gestión de reciclaje universitario con recompensas gamificadas

[![Java](https://img.shields.io/badge/Java-24-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-green.svg)](https://www.mongodb.com/cloud/atlas)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Descripción

**EcoCoins Campus** es un sistema integral de gestión de reciclaje para campus universitarios que incentiva a los estudiantes a reciclar mediante un sistema de recompensas gamificado. Los usuarios escanean códigos QR de contenedores inteligentes, registran materiales reciclados y ganan EcoCoins que pueden canjear por premios.

### ✨ Características Principales

- 🔐 **Autenticación JWT** - Login y registro seguro con tokens
- ♻️ **Gestión de Reciclajes** - Registro de materiales con cálculo automático de EcoCoins
- 📷 **Escaneo QR** - Validación de contenedores mediante códigos QR
- 🎁 **Sistema de Recompensas** - Catálogo de premios canjeables
- 📊 **Dashboard de Estadísticas** - Visualización de impacto ambiental
- 🏆 **Ranking de Usuarios** - Gamificación con niveles (Bronce, Plata, Oro, Platino)
- 🔔 **Notificaciones** - Sistema de alertas en tiempo real
- 📱 **API RESTful** - Endpoints documentados con Swagger

---

## 🛠️ Tecnologías

### Backend
- **Java 24** - Lenguaje de programación
- **Spring Boot 3.5.6** - Framework principal
- **Spring Security** - Autenticación y autorización
- **JWT (JJWT 0.12.3)** - Tokens de autenticación
- **MongoDB Atlas** - Base de datos NoSQL
- **Spring Data MongoDB** - ORM para MongoDB
- **Swagger/OpenAPI 3** - Documentación de API

### Herramientas
- **Maven** - Gestión de dependencias
- **Lombok** - Reducción de código boilerplate
- **Spring Boot Actuator** - Monitoreo de la aplicación
- **Spring DevTools** - Hot reload en desarrollo

---

## 📁 Estructura del Proyecto
```
src/main/java/com/ecocoins/ecocoins_microservice/
│
├── config/                    # Configuraciones
│   ├── CorsConfig.java
│   ├── SecurityConfig.java
│   └── SwaggerConfig.java
│
├── controller/                # Controladores REST
│   ├── AuthController.java
│   ├── CanjeController.java
│   ├── ContenedorController.java
│   ├── QrController.java
│   └── UsuarioController.java
│
├── dto/                       # Data Transfer Objects
│   ├── ApiResponse.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   └── RegisterRequest.java
│
├── exception/                 # Manejo de excepciones
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── UnauthorizedException.java
│
├── model/                     # Modelos de datos
│   ├── Usuario.java
│   ├── Reciclaje.java
│   ├── Recompensa.java
│   ├── Canje.java
│   ├── Contenedor.java
│   └── Notificacion.java
│
├── repository/                # Repositorios MongoDB
│   ├── UsuarioRepository.java
│   ├── ReciclajeRepository.java
│   └── RecompensaRepository.java
│
├── security/                  # Seguridad JWT
│   ├── JwtService.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
│
├── service/                   # Lógica de negocio
│   ├── AuthService.java
│   ├── CanjeService.java
│   ├── QrService.java
│   ├── NotificacionService.java
│   └── EmailService.java
│
└── util/                      # Utilidades
    └── ValidationUtil.java
```

---

## 🚀 Instalación y Configuración

### Prerrequisitos

- Java 24 o superior
- Maven 3.8+
- MongoDB Atlas (cuenta gratuita)
- IDE (IntelliJ IDEA recomendado)

### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/EcoCoins_Microservice.git
cd EcoCoins_Microservice
```

### 2. Configurar MongoDB

1. Crea una cuenta en [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Crea un cluster gratuito
3. Obtén tu connection string

### 3. Configurar Variables de Entorno

Edita `src/main/resources/application.properties`:
```properties
# MongoDB
spring.data.mongodb.uri=mongodb+srv://USER:PASSWORD@cluster.mongodb.net/ecocoinscampus

# JWT
jwt.secret=TU_CLAVE_SECRETA_BASE64_256_BITS
jwt.expiration=86400000

# Server
server.port=8080
```

### 4. Generar Clave JWT

Genera una clave secreta de 256 bits:
```bash
# En Linux/Mac
openssl rand -base64 32

# En Windows PowerShell
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($bytes)
[Convert]::ToBase64String($bytes)
```

### 5. Compilar y Ejecutar
```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

---

## 📚 Documentación de la API

### Swagger UI

Accede a la documentación interactiva en:
```
http://localhost:8080/swagger-ui.html
```

### Endpoints Principales

#### Autenticación
```http
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/validate?token={token}
```

#### Reciclajes
```http
GET  /api/reciclajes
POST /api/reciclajes
GET  /api/reciclajes/usuario/{usuarioId}
```

#### QR Code
```http
GET  /api/qr/validar/{codigo}
POST /api/qr/registrar
GET  /api/qr/tarifas
```

#### Canjes
```http
POST /api/canjes/canjear
GET  /api/canjes/usuario/{usuarioId}
GET  /api/canjes/{id}
```

#### Recompensas
```http
GET  /api/recompensas
GET  /api/recompensas/{id}
POST /api/recompensas
```

#### Usuarios
```http
GET  /api/usuarios
GET  /api/usuarios/{id}
PATCH /api/usuarios/{id}/estado
```

---

## 🔐 Autenticación

El sistema usa **JWT (JSON Web Tokens)** para autenticación.

### Flujo de Autenticación

1. **Registro o Login:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "correo": "usuario@tecsup.edu.pe",
  "contrasenia": "123456"
}
```

2. **Respuesta con Token:**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tipo": "Bearer",
    "id": "673abc123",
    "nombre": "Juan Pérez",
    "correo": "usuario@tecsup.edu.pe",
    "rol": "usuario",
    "ecoCoins": 150
  }
}
```

3. **Usar Token en Requests:**
```http
GET /api/usuarios
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 💰 Sistema de EcoCoins

### Tarifas por Material

| Material | EcoCoins por Kg |
|----------|-----------------|
| Plástico | 5 |
| Papel | 3 |
| Vidrio | 7 |
| Metal | 10 |
| Cartón | 4 |
| Electrónico | 15 |
| Orgánico | 2 |
| Pilas | 20 |

### Niveles de Usuario

| Nivel | EcoCoins Requeridas | Badge |
|-------|---------------------|-------|
| Bronce | 0 - 199 | 🥉 |
| Plata | 200 - 499 | 🥈 |
| Oro | 500 - 999 | 🥇 |
| Platino | 1000+ | 💎 |

---

## 🧪 Testing

### Ejecutar Tests
```bash
# Todos los tests
mvn test

# Tests específicos
mvn test -Dtest=AuthServiceTest
```

### Probar con Postman

1. Importa la colección de Postman (próximamente)
2. Configura la variable `base_url` = `http://localhost:8080`
3. Ejecuta la carpeta "Auth" para obtener un token
4. Usa el token para probar otros endpoints

---

## 📊 Monitoreo

### Actuator Endpoints
```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
http://localhost:8080/actuator/metrics
```

---

## 🐛 Troubleshooting

### Error: "Could not resolve placeholder 'jwt.secret'"

**Solución:** Verifica que `application.properties` tenga configurado `jwt.secret`

### Error: "Connection refused to MongoDB"

**Solución:** 
1. Verifica tu connection string
2. Asegúrate de permitir tu IP en MongoDB Atlas
3. Revisa que el usuario/contraseña sean correctos

### Error: 401 Unauthorized

**Solución:**
1. Verifica que el token no haya expirado
2. Asegura que el header `Authorization: Bearer TOKEN` esté presente
3. Confirma que el token sea válido

---

## 🤝 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

## 👥 Autores

- **Fabrizio Jiménez** - *Desarrollo Inicial* - [@fabriziojimenez](https://github.com/fabriziojimenez)

---

## 🙏 Agradecimientos

- Tecsup por la inspiración del proyecto
- MongoDB por la base de datos gratuita
- Spring Boot por el excelente framework
- Todos los contribuidores del proyecto

---

## 📞 Contacto

**Email:** contacto@ecocoinscampus.com  
**Website:** [ecocoinscampus.com](https://ecocoinscampus.com)  
**GitHub:** [github.com/tu-usuario/EcoCoins_Microservice](https://github.com/tu-usuario/EcoCoins_Microservice)

---

## 🗺️ Roadmap

- [x] Sistema de autenticación JWT
- [x] CRUD de usuarios y reciclajes
- [x] Sistema de recompensas
- [x] Escaneo de códigos QR
- [ ] Notificaciones push
- [ ] Panel de administración web
- [ ] Integración con app móvil
- [ ] Sistema de rankings global
- [ ] Reportes avanzados
- [ ] Inteligencia artificial para clasificación de materiales

---

**⭐ Si te gusta el proyecto, no olvides darle una estrella!**
