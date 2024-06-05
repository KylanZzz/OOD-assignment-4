package stock.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockDataSource implements DataSource {

  private Map<LocalDate, Boolean> stockExistence = new HashMap<>();
  private List<String> tickers = List.of("A", "GOOG", "AMZN");
  private List<LocalDate> dates = List.of(LocalDate.of(2024, 05, 04),
          LocalDate.of(2024, 5, 6),
          LocalDate.of(2024, 5, 7),
          LocalDate.of(2024,5,8),
          LocalDate.of(2024,5,9),
          LocalDate.of(2024,5,12),
          LocalDate.of(2024,5,15));
  private Map<LocalDate, Double> closingPrices = new HashMap<>();

  public void setStockExistsAtDate(LocalDate date) {
    boolean exists = false;
    if (dates.contains(date)) {
      exists = true;
    }

    stockExistence.put(date, exists);
  }

  public void setClosingPrice(LocalDate date, double price) {
    closingPrices.put(date, price);
  }

  @Override
  public double getClosingPrice(LocalDate date, String ticker) throws IOException {
    if (!tickers.contains(ticker)) {
      throw new IOException("There is no such ticker");
    }
    return closingPrices.getOrDefault(date, 0.0);
  }

  @Override
  public boolean stockExistsAtDate(LocalDate date, String ticker) throws IOException {
    if (!tickers.contains(ticker)) {
      throw new IOException("There is no such ticker");
    }
    return stockExistence.getOrDefault(date, false);
  }

  @Override
  public boolean stockInDataSource(String ticker) throws IOException {
    // For simplicity, assume the stock always exists in the data source
    if (tickers.contains(ticker)) {
      return true;
    } else {
      return false;
//      throw new IOException("There is no such ticker");
    }
  }
}
