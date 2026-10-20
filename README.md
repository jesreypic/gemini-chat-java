# Gemini Chat Java - Agente de Consola con I/O Local

Cliente de terminal interactivo desarrollado en **Java nativo (Java 11+)** para la interacción y procesamiento cognitivo mediante modelos avanzados de Google Gemini (`gemini-2.5-flash`, `gemini-1.5-flash`, `gemini-1.5-pro`).

El sistema implementa una arquitectura de **Agente de Software Local**, integrando ingestión y análisis de documentos locales, persistencia de memoria contextual multi-turno y exportación atómica de entregables al sistema de archivos del usuario.

---

## Características Principales

* **Java Nativo (Zero Dependencies):** Construido exclusivamente con librerías nativas del JDK (`java.net.http`, `java.nio.file`, `java.util`), eliminando la necesidad de gestores de dependencias externos (Maven/Gradle) para una ejecución ligera y sin sobrecarga.
* **Memoria Conversacional Dinámica:** Serialización y mantenimiento del contexto completo de la sesión mediante colecciones estructuradas de mensajes con roles de usuario (`user`) y modelo (`model`).
* **Ingestión de Archivos Locales (`/leer`):** Capacidad para cargar y sanitizar rutas locales (soporte nativo para rutas con espacios y comillas en Windows mediante `java.nio.file.Path`), inyectando el contenido en el flujo conversacional.
* **Exportación Atómica de Entregables (`/guardar`):** Persistencia directa de las respuestas generadas por el modelo en disco (soporte para `.txt`, `.md`, `.sql`, `.java`, etc.) mediante `Files.writeString()`.
* **Tolerancia a Fallos y Alta Disponibilidad (Fallback):** Mecanismo de reintento automático iterativo entre modelos de respaldo en caso de errores de red, latencia o cuotas agotadas.
* **Seguridad en Credenciales:** Carga desacoplada de la clave de API mediante variables de entorno del sistema (`GEMINI_API_KEY`), garantizando buenas prácticas de seguridad y evitando credenciales expuestas en el código fuente.

---

## Comandos Disponibles en Consola

| Comando | Sintaxis | Descripción |
| :--- | :--- | :--- |
| **Chat General** | `Cualquier texto...` | Conversación interactiva estándar con memoria contextual activa. |
| **Cargar Archivo** | `/leer <ruta_del_archivo>` | Lee un archivo de texto o código fuente local y lo envía a la IA para su análisis, resumen o auditoría. |
| **Exportar Salida** | `/guardar <nombre_archivo>` | Guarda la última respuesta generada por la IA en un archivo local en disco. |
| **Finalizar** | `salir` | Cierra la sesión interactiva y libera los recursos del proceso. |

---

## Ejemplo de Flujo de Trabajo (Agente Autónomo)

```text
=== CHAT CON IA EN JAVA (Escribe 'salir' para terminar) ===

Tú: /leer C:\Users\usuario\Desktop\prueba_ia.txt
-> Archivo cargado (746 caracteres). Enviando al modelo...
IA pensando...

IA (gemini-2.5-flash): He leído correctamente el contenido del archivo `prueba_ia.txt`. Aquí tienes un resumen de los puntos clave:
El documento detalla el estado de un Sistema de Gestión de Inventario para Pymes...

Tú: Escribe una propuesta técnica formal para solucionar el problema de concurrencia y optimizar la base de datos.
IA pensando...

IA (gemini-2.5-flash): Para solucionar el problema de bloqueos por concurrencia en tu Sistema de Gestión de Inventario...
[Genera el informe técnico completo]

Tú: /guardar propuesta_concurrencia.md
-> Éxito: Contenido exportado correctamente en: C:\Users\usuario\Desktop\propuesta_concurrencia.md
```

---

## Requisitos e Instalación

### 1. Requisitos Previos
* **Java Development Kit (JDK):** Versión 11 o superior.
* **Clave de API de Google Gemini:** Obtenible a través de Google AI Studio.

### 2. Configuración de Variables de Entorno
Configura tu clave de API en el sistema para que el cliente pueda leerla de forma segura:

```bash
# Windows (CMD / PowerShell)
setx GEMINI_API_KEY "tu_api_key_aqui"

# Linux / macOS
export GEMINI_API_KEY="tu_api_key_aqui"
```

### 3. Clonación y Ejecución
```bash
# Clonar el repositorio
git clone https://github.com/TU_USUARIO/gemini-chat-java.git
cd gemini-chat-java

# Compilar el código fuente
javac src/Cliente_IA.java

# Ejecutar el agente interactivo
java -cp src Cliente_IA
```

---

## Estructura del Proyecto

```text
gemini-chat-java/
├── src/
│   └── Cliente_IA.java   # Núcleo del agente: HTTP client, I/O local y REPL
├── README.md             # Documentación técnica del proyecto
└── .gitignore            # Exclusión de binarios y archivos del IDE
```

---

## Autor

Desarrollado por **Jesús Reyes**  
Estudiante de 1º de Desarrollo de Aplicaciones Multiplataforma (DAM).
