package pl.parser.nbp.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Wojtek on 2017-04-23.
 */
public class HttpClient {
    private String uri;

    public HttpClient(String uri) {
        this.uri = uri;
    }

    public InputStream getFile(String fileName) throws IOException {
        URL url = new URL(uri + fileName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "text/plain");

        return connection.getInputStream();
    }
}
