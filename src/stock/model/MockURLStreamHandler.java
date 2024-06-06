package stock.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * A custom URLStreamHandler used to mock URL connections for testing purposes.
 * This class allows for simulating network operations by providing predefined responses
 * without making actual HTTP connections. It's particularly useful in unit testing
 * where external network dependencies are to be avoided.
 */
public class MockURLStreamHandler extends URLStreamHandler {

  private String response;

  /**
   * Constructs a MockURLStreamHandler with a predefined response.
   *
   * @param response The response string to be returned by this handler,
   *                when a URL connection is opened.
   */
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