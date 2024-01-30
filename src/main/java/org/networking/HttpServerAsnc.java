package org.networking;

import org.json.JSONObject;

import java.net.*;
import java.io.*;

/**
 * Clase para manejar un servidor HTTP asíncrono.
 */
public class HttpServerAsnc {

    /**
     * Método principal que inicia el servidor y espera conexiones entrantes.
     * @param args argumentos de línea de comandos (no utilizados)
     * @throws IOException si ocurre un error de entrada/salida al abrir el servidor
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                final Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }
        serverSocket.close();
    }

    /**
     * Maneja la comunicación con un cliente conectado.
     * @param clientSocket el socket del cliente
     */
    static void handleClient(Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean firstLine = true;
            String method = "";
            URI uri = null;
            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    String[] requestParts = inputLine.split(" ");
                    method = requestParts[0];
                    uri = new URI(requestParts[1]);
                    System.out.println("Method: " + method);
                    System.out.println("URL: " + uri.toString());
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            if (uri.getPath().equals("/cliente")) {
                outputLine = obtainHtml();
            } else if (uri.getPath().equals("/movie")) {
                outputLine = obtainHtmlRequest(method, uri);
            } else {
                outputLine = errorHtml();
            }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Genera una respuesta HTML con la información de una solicitud de película.
     * @param method el método de la solicitud HTTP
     * @param uri la URI de la solicitud HTTP
     * @return una cadena que representa la respuesta HTML
     */
    private static String obtainHtmlRequest(String method, URI uri) {
        JSONObject jsonResponse = HttpRequestHandler.handleRequest(method, uri);

        String title = jsonResponse.optString("Title", "");
        String year = jsonResponse.optString("Year", "");
        String rated = jsonResponse.optString("Rated", "");
        String released = jsonResponse.optString("Released", "");
        String runtime = jsonResponse.optString("Runtime", "");
        String genre = jsonResponse.optString("Genre", "");
        String director = jsonResponse.optString("Director", "");
        String plot = jsonResponse.optString("Plot", "");
        String imdbRating = jsonResponse.optString("imdbRating", "");
        String poster = jsonResponse.optString("Poster", "");

        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "<head>\r\n"
                + "<title>Movies</title>\r\n"
                + "<style>\r\n"
                + "body { font-family: Arial, sans-serif; }\r\n"
                + "h1 { color: #333; }\r\n"
                + ".movie-details { margin-bottom: 20px; }\r\n"
                + ".movie-details img { max-width: 200px; }\r\n"
                + "</style>\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "<h1>" + title + " (" + year + ")</h1>\r\n"
                + "<div class=\"movie-details\">\r\n"
                + "<img src=\"" + poster + "\" alt=\"" + title + "\"> <br>\r\n"
                + "<strong>Rated:</strong> " + rated + "<br>\r\n"
                + "<strong>Released:</strong> " + released + "<br>\r\n"
                + "<strong>Runtime:</strong> " + runtime + "<br>\r\n"
                + "<strong>Genre:</strong> " + genre + "<br>\r\n"
                + "<strong>Director:</strong> " + director + "<br>\r\n"
                + "<strong>IMDb Rating:</strong> " + imdbRating + "<br>\r\n"
                + "<strong>Plot:</strong><br>\r\n"
                + "<p>" + plot + "</p>\r\n"
                + "</div>\r\n"
                + "</body>\r\n"
                + "</html>";
        return outputLine;
    }

    /**
     * Genera una respuesta HTML de error para una página no encontrada (404).
     * @return una cadena que representa la respuesta HTML de error
     */
    private static String errorHtml() {
        String outputLine = "HTTP/1.1 404 Not Found\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "    <head>\r\n"
                + "        <title>404 Not Found</title>\r\n"
                + "        <meta charset=\"ISO-8859-1\">\r\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                + "    </head>\r\n"
                + "    <body>\r\n"
                + "        <h1>Error</h1>\r\n"
                + "    </body>\r\n"
                + "</html>";
        return outputLine;
    }

    /**
     * Genera una respuesta HTML con un formulario para solicitar información sobre películas.
     * @return una cadena que representa la respuesta HTML del formulario
     */
    private static String obtainHtml() {
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "    <head>\r\n"
                + "        <title>Form Example</title>\r\n"
                + "        <meta charset=\"UTF-8\">\r\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                + "    </head>\r\n"
                + "    <body>\r\n"
                + "        <h1>OMDBAPI's Movies</h1>\r\n"
                + "        <form action=\"/cliente\">\r\n"
                + "            <label for=\"movie\">Movie:</label><br>\r\n"
                + "            <input type=\"text\" id=\"movie\" name=\"movie\" value=\"Guardians of the Galaxy\"><br><br>\r\n"
                + "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\r\n"
                + "        </form> \r\n"
                + "        <div id=\"getrespmsg\"></div>\r\n"
                + "\r\n"
                + "        <script>\n"
                + "            function loadGetMsg() {\n"
                + "                let movieVar = document.getElementById(\"movie\").value;\n"
                + "                const xhttp = new XMLHttpRequest();\n"
                + "                xhttp.onload = function() {\n"
                + "                    document.getElementById(\"getrespmsg\").innerHTML =\n"
                + "                    this.responseText;\n"
                + "                }\n"
                + "                xhttp.open(\"GET\", \"/movie?t=\" + encodeURIComponent(movieVar));\n"
                + "                xhttp.send();\n"
                + "            }\n"
                + "        </script>\r\n"
                + "    </body>\r\n"
                + "</html>";
        return outputLine;
    }


}
