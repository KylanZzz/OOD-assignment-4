package stock.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MockDataSource implements DataSource {

  private Map<LocalDate, Boolean> stockExistence = new HashMap<>();
  private Map<LocalDate, Double> closingPrices = new HashMap<>();

  public void setStockExistsAtDate(LocalDate date, boolean exists) {
    stockExistence.put(date, exists);
  }

  public void setClosingPrice(LocalDate date, double price) {
    closingPrices.put(date, price);
  }

  @Override
  public double getClosingPrice(LocalDate date, String ticker) throws IOException {
    return closingPrices.getOrDefault(date, 0.0);
  }

  @Override
  public boolean stockExistsAtDate(LocalDate date, String ticker) throws IOException {
    return stockExistence.getOrDefault(date, false);
  }

  @Override
  public boolean stockInDataSource(String ticker) throws IOException {
    // For simplicity, assume the stock always exists in the data source
    return true;
  }
}
