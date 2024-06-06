package stock.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mock implementation of the DataSource interface for testing purposes.
 * This class simulates stock data interactions without the need for an actual data source,
 * such as a database or external API.
 */
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


  /**
   * Sets the stock existence status for a given date.
   * This method simulates whether stock data is available for specific dates.
   *
   * @param date The date for which to set the stock existence.
   */
  public void setStockExistsAtDate(LocalDate date) {
    boolean exists = false;
    if (dates.contains(date)) {
      exists = true;
    }

    stockExistence.put(date, exists);
  }

  /**
   * Setting the closing price for the mock.
   * @param date  the date to set the price for.
   * @param price the price of the stock.
   */
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
    return tickers.contains(ticker);
  }
}
