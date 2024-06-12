package stock.model.portfolio;

import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BuyTransactionTest {

  @Test
  public void buyWorksWithEmptyPortfolio() {
    Map<String, Double> port = new HashMap<>();
    Transaction buy = new BuyTransaction(LocalDate.of(1,1,1), 5.0, "AAPL");

    port = buy.apply(port);
    Map<String, Double> expected = Map.of("AAPL", 5.0);
    assertEquals(expected, port);
  }

  @Test (expected = IllegalArgumentException.class)
  public void buyThrowsExceptionWhenSharesIsLessThanZero() {
    Map<String, Double> port = new HashMap<>();
    Transaction buy = new BuyTransaction(LocalDate.of(1,1,1), -1.0, "AAPL");
  }

  @Test
  public void buyWorksWithMultipleOfSameStock() {
    Map<String, Double> port = new HashMap<>();
    Transaction buy = new BuyTransaction(LocalDate.of(1,1,1), 5.0, "AAPL");
    Transaction buy2 = new BuyTransaction(LocalDate.of(1,2,2), 10.0, "AAPL");

    port = buy.apply(port);
    port = buy2.apply(port);

    Map<String, Double> expected = Map.of("AAPL", 15.0);
    assertEquals(expected, port);
  }

  @Test
  public void buyWorksWithMultipleOfDifferentStock() {
    Map<String, Double> port = new HashMap<>();
    Transaction buy = new BuyTransaction(LocalDate.of(1,1,1), 5.0, "AAPL");
    Transaction buy2 = new BuyTransaction(LocalDate.of(1,2,2), 1.5, "NFLX");
    Transaction buy3 = new BuyTransaction(LocalDate.of(1,2,2), 4.2, "AMZN");

    port = buy.apply(port);
    port = buy2.apply(port);
    port = buy3.apply(port);

    Map<String, Double> expected = Map.of("AAPL", 5.0, "NFLX", 1.5, "AMZN", 4.2);
    assertEquals(expected, port);
  }

}