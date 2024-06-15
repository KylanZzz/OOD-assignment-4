package stock.model.portfolio;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


/**
 * A class that helps to test the functionalities of SellTransaction class.
 */

public class SellTransactionTest {
  private static final double epsilon = 0.01;

  private boolean comparePort(Map<String, Double> map1, Map<String, Double> map2) {
    for (var key: map1.keySet()) {
      if (!map2.containsKey(key)) {
        return false;
      }
      if (Math.abs(map1.get(key) - map2.get(key)) > epsilon) {
        return false;
      }
    }
    return true;
  }

  @Test (expected = IllegalArgumentException.class)
  public void sellThrowsExceptionForSharesLessThanZero() {
    Map<String, Double> port = new HashMap<>();
    Transaction sell = new SellTransaction(LocalDate.of(2,2,2), -1.0, "AAPL");
  }

  @Test (expected = RuntimeException.class)
  public void sellThrowsExceptionForNoStockInPortfolio() {
    Map<String, Double> port = Map.of();
    Transaction sell = new SellTransaction(LocalDate.of(2,2,2), 10.0, "AAPL");
    port = sell.apply(port);
  }

  @Test (expected = RuntimeException.class)
  public void sellThrowsExceptionForNotEnoughShares() {
    Map<String, Double> port = Map.of("AAPL", 9.5);
    Transaction sell = new SellTransaction(LocalDate.of(2,2,2), 10.0, "AAPL");
    port = sell.apply(port);
  }

  @Test
  public void sellWorksOnce() {
    Map<String, Double> port = new HashMap<>();
    port.put("AAPL", 9.5);
    Transaction sell = new SellTransaction(LocalDate.of(2,2,2), 2.0, "AAPL");
    port = sell.apply(port);
    var expected = Map.of("AAPL", 7.5);
    assertTrue(comparePort(expected, port));
  }

  @Test
  public void sellWorksMultipleTimes() {
    Map<String, Double> port = new HashMap<>();
    port.put("AMZN", 3.0);
    port.put("ZZZZ", 5.0);
    port.put("LLLL", 10.0);
    Transaction sell = new SellTransaction(LocalDate.of(2,2,2), 2.0, "AMZN");
    Transaction sell2 = new SellTransaction(LocalDate.of(2,2,2), 3.5, "ZZZZ");
    Transaction sell3 = new SellTransaction(LocalDate.of(2,2,2), 5.6, "LLLL");
    Transaction sell4 = new SellTransaction(LocalDate.of(2,2,2), 3.0, "LLLL");

    port = sell.apply(port);
    var expected = Map.of("AMZN", 1.0, "ZZZZ", 5.0, "LLLL", 10.0);
    assertTrue(comparePort(expected, port));

    port = sell2.apply(port);
    expected = Map.of("AMZN", 1.0, "ZZZZ", 1.5, "LLLL", 10.0);
    assertTrue(comparePort(expected, port));

    port = sell3.apply(port);
    expected = Map.of("AMZN", 1.0, "ZZZZ", 1.5, "LLLL", 4.4);
    assertTrue(comparePort(expected, port));

    port = sell4.apply(port);
    expected = Map.of("AMZN", 1.0, "ZZZZ", 1.5, "LLLL", 1.4);
    assertTrue(comparePort(expected, port));
  }

  @Test
  public void sellWorksWhenSharesReachZero() {
    Map<String, Double> port = new HashMap<>();
    port.put("AAPL", 9.5);
    Transaction sell = new SellTransaction(LocalDate.of(2,2,2), 2.0, "AAPL");
    port = sell.apply(port);
    var expected = Map.of("AAPL", 7.5);
    assertTrue(comparePort(expected, port));

    Transaction sell2 = new SellTransaction(LocalDate.of(2,2,2), 7.5, "AAPL");
    port = sell2.apply(port);
    expected = Map.of();
    assertTrue(comparePort(expected, port));
  }

  @Test
  public void sellTransactionSavesCorrectly() {
    Transaction sell = new SellTransaction(LocalDate.of(2024, 6, 13), 20.0, "NFLX");
    String expected = "SELL:06/13/2024,20.0,NFLX";
    assertEquals(expected, sell.save());
  }

  @Test
  public void sellTransactionConstructedFromSaveStringWorks() throws IOException {
    String data = "SELL:06/13/2024,20.0,NFLX";
    Transaction sell = new SellTransaction(data);

    Map<String, Double> port = new HashMap<>();
    port.put("NFLX", 30.0);
    port = sell.apply(port);

    Map<String, Double> expected = Map.of("NFLX", 10.0);
    assertTrue(comparePort(expected, port));
  }

  @Test
  public void sellTransactionSaveAndReconstruct() throws IOException {
    Transaction originalSell = new SellTransaction(LocalDate.of(2024, 6, 13), 20.0, "NFLX");
    String saveString = originalSell.save();

    Transaction reconstructedSell = new SellTransaction(saveString);

    Map<String, Double> originalPort = new HashMap<>();
    Map<String, Double> reconstructedPort = new HashMap<>();
    originalPort.put("NFLX", 30.0);
    reconstructedPort.put("NFLX", 30.0);

    originalPort = originalSell.apply(originalPort);
    reconstructedPort = reconstructedSell.apply(reconstructedPort);

    assertTrue(comparePort(originalPort, reconstructedPort));
  }
}