import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cliente_IA {

    // Cargamos la clave directamente desde el archivo protegido
    private static final String API_KEY = cargarApiKey();

    static class Mensaje {
        String rol;
        String texto;

        Mensaje(String rol, String texto) {
            this.rol = rol;
            this.texto = texto;
        }
    }

    public static void main(String[] args) {
        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            System.err.println("Error: No se ha podido cargar la API Key desde config.properties");
            return;
        }

        Scanner teclado = new Scanner(System.in);
        List<Mensaje> historial = new ArrayList<>();

        String[] modelos = {
            "gemini-3-flash-preview",
            "gemini-3.8-flash",
            "gemini-3.1-flash-lite"
        };

        HttpClient cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        System.out.println("=== CHAT CON IA EN JAVA (Escribe 'salir' para terminar) ===");

        while (true) {
            System.out.print("\nTú: ");
            String prompt = teclado.nextLine().trim();

            if (prompt.equalsIgnoreCase("salir")) {
                System.out.println("Cerrando asistente...");
                break;
            }

            if (prompt.isEmpty()) {
                continue;
            }

            historial.add(new Mensaje("user", prompt));
            String cuerpoJson = construirJsonHistorial(historial);

            boolean resuelto = false;
            System.out.println("IA pensando...");

            for (String modelo : modelos) {
                if (resuelto) break;

                String url = "https://generativelanguage.googleapis.com/v1beta/models/" + modelo + ":generateContent";

                HttpRequest peticion = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .header("X-goog-api-key", API_KEY)
                        .timeout(Duration.ofSeconds(20))
                        .POST(HttpRequest.BodyPublishers.ofString(cuerpoJson))
                        .build();

                try {
                    HttpResponse<String> respuesta = cliente.send(peticion, HttpResponse.BodyHandlers.ofString());

                    if (respuesta.statusCode() == 200) {
                        String textoRespuesta = extraerTexto(respuesta.body());
                        System.out.println("\nIA (" + modelo + "): " + textoRespuesta);
                        historial.add(new Mensaje("model", textoRespuesta));
                        resuelto = true;
                    }
                } catch (Exception e) {
                    // Si hay corte de red o timeout, prueba con el siguiente modelo de respaldo
                }
            }

            if (!resuelto) {
                System.out.println("\nIA: Lo siento, no se pudo obtener respuesta en este momento.");
                historial.remove(historial.size() - 1);
            }
        }

        teclado.close();
    }

    // Lee la clave de forma segura desde config.properties
    private static String cargarApiKey() {
        Properties prop = new Properties();
        try (FileInputStream entrada = new FileInputStream("config.properties")) {
            prop.load(entrada);
            return prop.getProperty("API_KEY");
        } catch (IOException e) {
            return null;
        }
    }

    private static String construirJsonHistorial(List<Mensaje> historial) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"contents\": [");

        for (int i = 0; i < historial.size(); i++) {
            Mensaje m = historial.get(i);
            String textoEscapado = m.texto
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "");

            sb.append("{\"role\": \"").append(m.rol).append("\", ");
            sb.append("\"parts\": [{\"text\": \"").append(textoEscapado).append("\"}]}");

            if (i < historial.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("]}");
        return sb.toString();
    }

 // Extracción lineal sin Regex para evitar desbordamiento de pila (StackOverflowError)
    private static String extraerTexto(String json) {
        String clave = "\"text\": \"";
        int inicio = json.indexOf(clave);
        if (inicio == -1) return json;

        inicio += clave.length();
        StringBuilder resultado = new StringBuilder();
        boolean escape = false;

        for (int i = inicio; i < json.length(); i++) {
            char c = json.charAt(i);

            if (escape) {
                switch (c) {
                    case 'n': resultado.append('\n'); break;
                    case 't': resultado.append('\t'); break;
                    case 'r': break;
                    case '"': resultado.append('\"'); break;
                    case '\\': resultado.append('\\'); break;
                    default: resultado.append(c); break;
                }
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else if (c == '"') {
                break; // Fin del campo "text"
            } else {
                resultado.append(c);
            }
        }

        return resultado.toString();
    
    }
}