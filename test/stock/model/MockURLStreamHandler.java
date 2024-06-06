package stock.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class MockURLStreamHandler extends URLStreamHandler {

  private String response;

  public MockURLStreamHandler(String response) {
    this.response = response;
  }

  @Override
  protected URLConnection openConnection(URL u) throws IOException {
    return new URLConnection(u) {
      @Override
      public void connect() throws IOException {
        // No actual connection is made
      }

      @Override
      public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(response.getBytes());
      }
    };
  }
}