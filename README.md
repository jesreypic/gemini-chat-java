# Gemini Chat Java (CLI Assistant)

Cliente de Inteligencia Artificial para consola desarrollado en **Java puro (Java 11+)** sin librerías externas de terceros. Permite mantener una conversación interactiva con modelos de lenguaje de Google Gemini mediante consumo directo de la API REST, implementando memoria conversacional multi-turno y tolerancia a fallos.

---

## Características Técnicas

- **Java Nativo (Zero Dependencies):** Uso exclusivo de la biblioteca estándar de Java (`java.net.http.HttpClient`, `HttpRequest`, `HttpResponse`) para orquestar peticiones HTTP.
- **Memoria Conversacional (Context Window):** Mantenimiento del historial acumulado (`user` / `model`) serializado dinámicamente en formato JSON para dotar al asistente de contexto en cada turno.
- **Parser Iterativo Lineal:** Extracción y saneamiento de texto en tiempo lineal sin recursión, diseñado para evitar desbordamientos de pila (`StackOverflowError`) asociados al backtracking de expresiones regulares complejas.
- **Tolerancia a Fallos y Fallback:** Lógica de degradación elegante que alterna automáticamente entre modelos (`gemini-3-flash-preview`, `gemini-3.8-flash`, etc.) ante incidencias de saturación de servidores (HTTP 503) o límites de tasa (HTTP 429).
- **Seguridad y Aislamiento de Credenciales:** Carga dinámica de la API Key mediante `Properties` desde un archivo local `config.properties` excluido del control de versiones vía `.gitignore`.

---

## Requisitos

- Java JDK 11 o superior.
- Una API Key válida de [Google AI Studio](https://aistudio.google.com/).

---

## Configuración e Instalación

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/jesreypic/gemini-chat-java.git](https://github.com/jesreypic/gemini-chat-java.git)
   cd gemini-chat-java

   Configurar la API Key:
   
2. Crea un archivo llamado config.properties en la raíz del proyecto con la siguiente estructura:

API_KEY=tu_api_key_aqui


3. Ejecución:

Abre el proyecto en tu IDE preferido (Eclipse, IntelliJ IDEA, VS Code).

Ejecuta la clase Cliente_IA.java.

Escribe salir en la consola en cualquier momento para finalizar la sesión.


Autor
Jesús Reyes - Estudiante de Desarrollo de Aplicaciones Multiplataforma (DAM).
