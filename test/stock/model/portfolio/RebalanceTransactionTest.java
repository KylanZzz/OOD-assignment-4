package stock.model.portfolio;

import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RebalanceTransactionTest {

  @Test
  public void rebalanceWorksOnEmptyPort() {
    var res = new HashMap<String, Double>();
    Map<String, Double> prices = Map.of();
    Map<String, Double> proportions = Map.of();
    var reb = new RebalanceTransaction(LocalDate.of(1,1,1), prices, proportions);
    reb.apply(res);

    assertEquals(Map.of(), res);
  }

  @Test
  public void rebalanceWorksOnSingleStock() {
    var res = new HashMap<String, Double>();
    res.put("AAPL", 10.0);
    Map<String, Double> prices = Map.of("AAPL", 30.0);
    Map<String, Double> proportions = Map.of("AAPL", 1.0);
    var reb = new RebalanceTransaction(LocalDate.of(1,1,1), prices, proportions);
    reb.apply(res);

    assertEquals(Map.of("AAPL", 10.0), res);
  }

  @Test
  public void rebalanceWorksOnMultipleStocks() {
    var res = new HashMap<String, Double>();

    res.put("A", 10.0);
    res.put("B", 20.0);
    res.put("C", 30.0);

    Map<String, Double> prices = Map.of("A", 2.0, "B", 4.0, "C", 6.0);
    Map<String, Double> proportions = Map.of("A", 0.1, "B", 0.4, "C", 0.5);
    var reb = new RebalanceTransaction(LocalDate.of(1,1,1), prices, proportions);
    reb.apply(res);

    // total = 10 * 2 + 20 * 4 + 6 * 30 = 20 + 80 + 180 = $280
    double total = res.get("A") * 2.0 + res.get("B") * 4.0 + res.get("C") * 6.0;

    assertEquals(280.0, total, 0.01);
    // 280 * 0.5 = 140
    assertEquals(140.0, res.get("C") * 6.0, 0.01);
    // 280 * 0.4 = 112
    assertEquals(112.0, res.get("B") * 4.0, 0.01);
    // 280 * 0.1 = 28
    assertEquals(28.0, res.get("A") * 2.0, 0.01);
  }

  @Test (expected = RuntimeException.class)
  public void rebalanceThrowsExceptionWhenProportionsAreGreaterThanZero() {
    var res = new HashMap<String, Double>();

    res.put("A", 10.0);
    res.put("B", 20.0);
    res.put("C", 30.0);

    Map<String, Double> prices = Map.of("A", 2.0, "B", 4.0, "C", 6.0);
    // proportions don't add up to 1.0          0.1 + 0.5 + 0.5 = 1.1 != 1.0
    Map<String, Double> proportions = Map.of("A", 0.1, "B", 0.5, "C", 0.5);
    var reb = new RebalanceTransaction(LocalDate.of(1,1,1), prices, proportions);
    reb.apply(res);
  }

  @Test (expected = RuntimeException.class)
  public void rebalanceWithZeroProportionsThrowsException() {
    var res = new HashMap<String, Double>();
    res.put("AAPL", 10.0);
    Map<String, Double> prices = Map.of("AAPL", 30.0);
    Map<String, Double> proportions = Map.of("AAPL", 0.0);
    var reb = new RebalanceTransaction(LocalDate.of(1,1,1), prices, proportions);
    reb.apply(res);

    assertEquals(0.0, res.get("AAPL"), 0.01);
  }

  @Test (expected = RuntimeException.class)
  public void rebalanceWithNegativeProportionsThrowsException() {
    var res = new HashMap<String, Double>();
    res.put("AAPL", 10.0);
    Map<String, Double> prices = Map.of("AAPL", 30.0);
    Map<String, Double> proportions = Map.of("AAPL", -0.5);
    var reb = new RebalanceTransaction(LocalDate.of(1,1,1), prices, proportions);
    reb.apply(res);
  }

  @Test (expected = RuntimeException.class)
  public void rebalanceWithExcessiveProportionsThrowsException() {
    var res = new HashMap<String, Double>();
    res.put("AAPL", 10.0);
    Map<String, Double> prices = Map.of("AAPL", 30.0);
    Map<String, Double> proportions = Map.of("AAPL", 1.5);
    var reb = new RebalanceTransaction(LocalDate.of(1,1,1), prices, proportions);
    reb.apply(res);
  }

  @Test
  public void rebalanceWithEqualProportions() {
    var res = new HashMap<String, Double>();
    res.put("AAPL", 10.0);
    res.put("GOOG", 20.0);
    Map<String, Double> prices = Map.of("AAPL", 30.0, "GOOG", 50.0);
    Map<String, Double> proportions = Map.of("AAPL", 0.5, "GOOG", 0.5);
    var reb = new RebalanceTransaction(LocalDate.of(1,1,1), prices, proportions);
    reb.apply(res);

    double totalValue = res.get("AAPL") * prices.get("AAPL") + res.get("GOOG") * prices.get("GOOG");

    assertEquals((totalValue * 0.5) / prices.get("AAPL"), res.get("AAPL"), 0.01);
    assertEquals((totalValue * 0.5) / prices.get("GOOG"), res.get("GOOG"), 0.01);
  }

  @Test
  public void rebalanceDoesNothingWhenProportionsMatchCurrentState() {
    var res = new HashMap<String, Double>();
    res.put("AAPL", 10.0);
    res.put("GOOG", 20.0);
    res.put("MSFT", 30.0);

    Map<String, Double> prices = Map.of("AAPL", 30.0, "GOOG", 50.0, "MSFT", 40.0);
    double totalValue = res.get("AAPL") * prices.get("AAPL") + res.get("GOOG") * prices.get("GOOG") + res.get("MSFT") * prices.get("MSFT");
    Map<String, Double> proportions = Map.of(
            "AAPL", (res.get("AAPL") * prices.get("AAPL")) / totalValue,
            "GOOG", (res.get("GOOG") * prices.get("GOOG")) / totalValue,
            "MSFT", (res.get("MSFT") * prices.get("MSFT")) / totalValue
    );

    var reb = new RebalanceTransaction(LocalDate.of(1,1,1), prices, proportions);
    reb.apply(res);

    assertEquals(10.0, res.get("AAPL"), 0.01);
    assertEquals(20.0, res.get("GOOG"), 0.01);
    assertEquals(30.0, res.get("MSFT"), 0.01);
  }

}