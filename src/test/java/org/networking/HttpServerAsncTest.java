package org.networking;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import static org.junit.Assert.*;

public class HttpServerAsncTest {

    @Test
    public void testHandleClient() throws IOException {
        // Preparamos la entrada y la salida para simular una conexión del cliente
        String request = "GET /cliente HTTP/1.1\r\nHost: localhost:35000\r\nConnection: keep-alive\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Creamos un socket de prueba utilizando las entradas y salidas simuladas
        Socket socket = new Socket() {
            @Override
            public synchronized OutputStream getOutputStream() throws IOException {
                return outputStream;
            }

            @Override
            public synchronized InputStream getInputStream() throws IOException {
                return inputStream;
            }
        };

        // Manejamos la solicitud del cliente
        HttpServerAsnc.handleClient(socket);

        // Verificamos que la respuesta sea la esperada
        String response = outputStream.toString();
        assertTrue(response.contains("OMDBAPI's Movies"));
        assertTrue(response.contains("Guardians of the Galaxy"));
    }
}

