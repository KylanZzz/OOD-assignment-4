package stock.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlphaVantageDataSourceTest {

  private MockAlphaVantageDataSource dataSource;

  @Before
  public void setup() {
    dataSource = new MockAlphaVantageDataSource();
    setupMockURLStreamHandlerFactory();
  }

  private void setupMockURLStreamHandlerFactory() {
    URLStreamHandlerFactory factory = protocol -> {
      if ("http".equals(protocol) || "https".equals(protocol)) {
        return new MockURLStreamHandler("date,open,high,low,close,volume\n2021-01-01,132.43,133.00,131.10,132.69,1000000");
      }
      return null;
    };

    try {
      Field factoryField = URL.class.getDeclaredField("factory");
      factoryField.setAccessible(true);
      factoryField.set(null, factory);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testInit() throws IOException {
    dataSource.init();
    assertTrue(dataSource.getTickerList().contains("AAPL"));
    assertTrue(dataSource.getTickerList().contains("MSFT"));
  }

  @Test
  public void testGenerateTickerList() throws IOException {
    dataSource.generateTickerList(new File("res/stocksData"));  // Pass a valid directory
    assertTrue(dataSource.getTickerList().contains("AAPL"));
    assertTrue(dataSource.getTickerList().contains("MSFT"));
  }

  @Test(expected = IOException.class)
  public void testGenerateTickerListFailed() throws IOException {
    dataSource.generateTickerList(new File("res/folder"));
  }

  @Test
  public void testGenerateStockCSV() throws Exception {
    File tempFolder = new File("tempTestFolder");
    tempFolder.mkdirs(); // Ensure the directory is created

    dataSource.generateStockCSV(tempFolder, "AAPL");

    File expectedFile = new File(tempFolder, "AAPL.csv");
    assertTrue(expectedFile.exists());
    assertTrue(expectedFile.length() > 0);

    String content = new String(Files.readAllBytes(Paths.get(expectedFile.getPath())));
    assertTrue(content.contains("132.43"));

    expectedFile.delete();
    tempFolder.delete();
  }

  @Test
  public void testStockExistsAtDate() {
    boolean exists = dataSource.stockExistsAtDate(LocalDate.of(2021, 1, 1), "AAPL");
    assertTrue(exists);
  }

  @Test
  public void testGetClosingPrice() {
    double price = dataSource.getClosingPrice(LocalDate.of(2021, 1, 1), "AAPL");
    assertEquals(132.69, price, 0.01);
  }

  @Test
  public void testGetClosingPriceNotExists() {
    double price = dataSource.getClosingPrice(LocalDate.of(2020, 1, 1), "A");
    assertEquals(0, price, 0.01);
  }

  @Test
  public void testStockInDataSource() {
    boolean inDataSource = dataSource.stockInDataSource("AAPL");
    assertTrue(inDataSource);
  }

  @Test(expected = MalformedURLException.class)
  public void createStockDataURLMock() throws MalformedURLException {
    dataSource.createStockDataURLMock("AAPL");
  }



}
