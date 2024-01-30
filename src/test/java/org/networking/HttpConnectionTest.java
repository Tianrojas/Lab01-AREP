package org.networking;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.json.JSONObject;

import java.io.IOException;

public class HttpConnectionTest {

    private HttpConnection httpConnection;

    @Before
    public void setUp() {
        httpConnection = new HttpConnection();
    }

    @Test
    public void testBuildURL() {
        String movieName = "Knives Out";
        String expectedURL = "https://www.omdbapi.com/?apikey=e687741e&t=Knives Out";
        assertEquals(expectedURL, httpConnection.buildURL(movieName));
    }

    @Test
    public void testSetMovieName() throws IOException {
        String movieName = "Knives Out";
        httpConnection.setMovieName(movieName);
        JSONObject movieJSN = httpConnection.getMovieJSN();
        assertEquals(movieName, movieJSN.getString("Title"));
    }
}
