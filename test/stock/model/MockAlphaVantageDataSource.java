package stock.model;

import java.io.*;
import java.lang.reflect.Field;
import java.net.*;
import java.time.LocalDate;
import java.util.*;

public class MockAlphaVantageDataSource extends AlphaVantageDataSource {
  private Map<String, Map<LocalDate, Double>> simulatedStockData = new HashMap<>();
  private Map<String, String> simulatedAPIResponses = new HashMap<>();

  public MockAlphaVantageDataSource() {
    super();
    initializeSimulatedData();
  }

  private void initializeSimulatedData() {
    // Initialize simulated API responses
    simulatedAPIResponses.put("AAPL", "date,open,high,low,close,volume\n2021-01-01,132.43,133.00,131.10,132.69,1000000");

    // Initialize simulated stock data
    Map<LocalDate, Double> aaplPrices = new HashMap<>();
    aaplPrices.put(LocalDate.of(2021, 1, 1), 132.69);
    simulatedStockData.put("AAPL", aaplPrices);

    // Add tickers to the ticker list using reflection
    try {
      Field tickerListField = AlphaVantageDataSource.class.getDeclaredField("tickerList");
      tickerListField.setAccessible(true);
      Set<String> tickerList = (Set<String>) tickerListField.get(this);
      tickerList.add("AAPL");
      tickerList.add("MSFT");
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException("Failed to access tickerList field", e);
    }
  }

  @Override
  protected void generateStockCSV(File folder, String ticker) throws IOException {
    String response = simulatedAPIResponses.getOrDefault(ticker, "");
    File outputFile = new File(folder, ticker + ".csv");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
      writer.write(response);
    }
  }

  protected void generateStockCSVMock(File folder, String ticker) throws IOException {
    String response = simulatedAPIResponses.getOrDefault(ticker, "");
    File outputFile = new File(folder, ticker + ".csv");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
      writer.write(response);
    }
  }

  @Override
  public URL createStockDataURL(String ticker) throws MalformedURLException {
    return new URL("http://localhost:8080/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + ticker + "&apikey=demo&datatype=csv");
  }

  public URL createStockDataURLMock(String ticker) throws MalformedURLException {
    return new URL("htttp://www.example.com" + ticker);
  }


  @Override
  public boolean stockExistsAtDate(LocalDate date, String ticker) {
    return simulatedStockData.containsKey(ticker) && simulatedStockData.get(ticker).containsKey(date);
  }

  @Override
  public double getClosingPrice(LocalDate date, String ticker) {
    return stockExistsAtDate(date, ticker) ? simulatedStockData.get(ticker).get(date) : 0;
  }

  @Override
  public boolean stockInDataSource(String ticker) {
    try {
      Field tickerListField = AlphaVantageDataSource.class.getDeclaredField("tickerList");
      tickerListField.setAccessible(true);
      Set<String> tickerList = (Set<String>) tickerListField.get(this);
      return tickerList.contains(ticker);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException("Failed to access tickerList field", e);
    }
  }

  @Override
  protected void deleteFolder(File folder) {
    System.out.println("Folder deletion simulated for: " + folder.getPath());
  }
}



