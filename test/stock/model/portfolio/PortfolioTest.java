package stock.model.portfolio;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.*;

public class PortfolioTest {
  @Test
  public void getNameWorks() {
    var port = new Portfolio("portfolio1");
    assertEquals("portfolio1", port.getName());
  }

  @Test
  public void renameWorks() {
    var port = new Portfolio("portfolio1");
    port.rename("port2");
    assertEquals("port2", port.getName());
  }

  @Test
  public void getCompositionWorksForEmptyPort() {
    var port = new Portfolio("portfolio1");
    assertEquals(Map.of(), port.getComposition(LocalDate.of(1,1,1)));
  }

  @Test
  public void getValueWorksForEmptyPort() {
    var port = new Portfolio("portfolio1");
    assertEquals((Double)0.0, port.getValue(LocalDate.of(1,1,1), Map.of("AAPL", 5.0)));
  }

  @Test
  public void getDistributionWorksForEmptyPort() {
    var port = new Portfolio("portfolio1");
    assertEquals(Map.of(), port.getDistribution(LocalDate.of(1,1,1), Map.of("AAPL", 155.5)));
  }

  @Test
  public void getCompositionWorksForNonEmptyPort() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.buyStock("MSFT", LocalDate.of(1, 1, 4), 30.0);
    assertEquals(Map.of("AAPL", 10.0, "GOOG", 20.0, "MSFT", 30.0), port.getComposition(LocalDate.of(1, 1, 4)));
  }

  @Test
  public void getValueWorksForNonEmptyPort() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.buyStock("MSFT", LocalDate.of(1, 1, 4), 30.0);
    Map<String, Double> prices = Map.of("AAPL", 150.0, "GOOG", 250.0, "MSFT", 350.0);
    double expectedValue = 10.0 * 150.0 + 20.0 * 250.0 + 30.0 * 350.0;
    assertEquals(expectedValue, port.getValue(LocalDate.of(1, 1, 4), prices), 0.01);
  }

  @Test
  public void getDistributionWorksForNonEmptyPort() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.buyStock("MSFT", LocalDate.of(1, 1, 4), 30.0);
    Map<String, Double> prices = Map.of("AAPL", 150.0, "GOOG", 250.0, "MSFT", 350.0);
    Map<String, Double> expectedDistribution = Map.of(
            "AAPL", 10.0 * 150.0,
            "GOOG", 20.0 * 250.0,
            "MSFT", 30.0 * 350.0
    );
    assertEquals(expectedDistribution, port.getDistribution(LocalDate.of(1, 1, 4), prices));
  }

  @Test
  public void getCompositionWorksAfterBuyingAndSelling() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 4), 5.0);
    assertEquals(Map.of("AAPL", 5.0, "GOOG", 20.0), port.getComposition(LocalDate.of(1, 1, 4)));
  }

  @Test
  public void getValueWorksAfterBuyingAndSelling() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 4), 5.0);
    Map<String, Double> prices = Map.of("AAPL", 150.0, "GOOG", 250.0);
    double expectedValue = 5.0 * 150.0 + 20.0 * 250.0;
    assertEquals(expectedValue, port.getValue(LocalDate.of(1, 1, 4), prices), 0.01);
  }

  @Test
  public void getDistributionWorksAfterBuyingAndSelling() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 4), 5.0);
    Map<String, Double> prices = Map.of("AAPL", 150.0, "GOOG", 250.0);
    Map<String, Double> expectedDistribution = Map.of(
            "AAPL", 5.0 * 150.0,
            "GOOG", 20.0 * 250.0
    );
    assertEquals(expectedDistribution, port.getDistribution(LocalDate.of(1, 1, 4), prices));
  }

  @Test (expected =  IllegalArgumentException.class)
  public void sellStockThrowsExceptionWhenStockNotInPort() {
    var port = new Portfolio("portfolio1");
    port.sellStock("AAPL", LocalDate.of(2004,12,20), 30.0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void sellStockThrowsExceptionWhenNotEnough() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(2003,12,20), 25.0);
    port.sellStock("AAPL", LocalDate.of(2004,12,20), 30.0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void sellStockThrowsExceptionWhenSellingBeforeBought() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(2,2,2), 40.0);
    port.sellStock("AAPL", LocalDate.of(1,1,1), 30.0);
  }

  @Test
  public void buyStockWorksForOne() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(2,2,2), 40.0);
    assertEquals(Map.of("AAPL", 40.0), port.getComposition(LocalDate.of(2,2,2)));
  }

  @Test
  public void buyStockWorksForMultiple() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1,1,2), 40.0);
    port.buyStock("AAPL", LocalDate.of(1,1,4), 20.0);
    port.buyStock("AMZN", LocalDate.of(1,1,4), 10.0);

    // This is bought after 1,1,5 so it should not be in portfolio
    port.buyStock("NFLX", LocalDate.of(1,1,6), 10.0);
    assertEquals(Map.of("AAPL", 60.0, "AMZN", 10.0),
            port.getComposition(LocalDate.of(1,1,5)));
  }

  @Test
  public void buyWorksForOutOfOrder() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(2,2,2), 40.0);
    assertEquals(Map.of("AAPL", 40.0), port.getComposition(LocalDate.of(2,2,10)));

    port.buyStock("AAPL", LocalDate.of(2,2,3), 50.0);
    assertEquals(Map.of("AAPL", 90.0), port.getComposition(LocalDate.of(2,2,10)));
  }

  @Test
  public void sellWorksForExactAmount() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1,1,2), 40.0);
    port.sellStock("AAPL", LocalDate.of(1,1,4), 40.0);
    assertEquals(Map.of(), port.getComposition(LocalDate.of(1,1,4)));
  }

  @Test
  public void sellWorksForInexactAmount() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1,1,2), 40.0);
    port.sellStock("AAPL", LocalDate.of(1,1,4), 35.0);
    assertEquals(Map.of("AAPL", 5.0), port.getComposition(LocalDate.of(1,1,4)));
  }

  @Test
  public void sellWorksForOutOfOrder() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1,1,2), 40.0);
    port.sellStock("AAPL", LocalDate.of(1,1,4), 35.0);
    port.sellStock("AAPL", LocalDate.of(1,1,5), 5.0);
    assertEquals(Map.of(), port.getComposition(LocalDate.of(1,1,5)));

    // Buy more stocks in between previous transactions
    port.buyStock("AAPL", LocalDate.of(1,1,3), 100.0);
    assertEquals(Map.of("AAPL", 100.0), port.getComposition(LocalDate.of(1,1,5)));
  }

  @Test (expected = IllegalArgumentException.class)
  public void rebalanceThrowsExceptionIfProportionsMissingStocks() {
    var port = new Portfolio("portfolio1");
    port.buyStock("A", LocalDate.of(1,1,2), 10.0);
    port.buyStock("B", LocalDate.of(1,1,3), 20.0);
    Map<String, Double> prices = Map.of("A", 30.0, "B", 20.0);
    Map<String, Double> proportions = Map.of("B", 0.5);
    port.rebalance(LocalDate.of(1,1,4), prices, proportions);
  }

  @Test
  public void rebalanceWorksForOneStock() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 40.0);
    Map<String, Double> prices = Map.of("AAPL", 30.0);
    Map<String, Double> proportions = Map.of("AAPL", 1.0);
    port.rebalance(LocalDate.of(1, 1, 3), prices, proportions);
    assertEquals(Map.of("AAPL", 40.0), port.getComposition(LocalDate.of(1, 1, 3)));
  }

  @Test
  public void rebalanceWorksForMultipleStocks() {
    var port = new Portfolio("portfolio1");
    port.buyStock("A", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("B", LocalDate.of(1, 1, 3), 20.0);
    port.buyStock("C", LocalDate.of(1, 1, 4), 30.0);

    Map<String, Double> prices = Map.of("A", 2.0, "B", 4.0, "C", 6.0);
    Map<String, Double> proportions = Map.of("A", 0.1, "B", 0.4, "C", 0.5);
    port.rebalance(LocalDate.of(1, 1, 5), prices, proportions);

    var composition = port.getComposition(LocalDate.of(1, 1, 5));

    double totalValue = composition.get("A") * prices.get("A") + composition.get("B") * prices.get("B") + composition.get("C") * prices.get("C");
    assertEquals(280.0, totalValue, 0.01);

    assertEquals(28.0, composition.get("A") * prices.get("A"), 0.01);
    assertEquals(112.0, composition.get("B") * prices.get("B"), 0.01);
    assertEquals(140.0, composition.get("C") * prices.get("C"), 0.01);
  }

  @Test
  public void rebalanceDoesNothingWhenProportionsMatchCurrentState() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.buyStock("MSFT", LocalDate.of(1, 1, 4), 30.0);

    Map<String, Double> prices = Map.of("AAPL", 30.0, "GOOG", 50.0, "MSFT", 40.0);
    double totalValue = 10.0 * 30.0 + 20.0 * 50.0 + 30.0 * 40.0;

    Map<String, Double> proportions = Map.of(
            "AAPL", (10.0 * 30.0) / totalValue,
            "GOOG", (20.0 * 50.0) / totalValue,
            "MSFT", (30.0 * 40.0) / totalValue
    );

    port.rebalance(LocalDate.of(1, 1, 5), prices, proportions);

    assertEquals(Map.of("AAPL", 10.0, "GOOG", 20.0, "MSFT", 30.0), port.getComposition(LocalDate.of(1, 1, 5)));
  }

  @Test
  public void rebalanceWorksForEdgeCaseZeroProportions() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    Map<String, Double> prices = Map.of("AAPL", 30.0, "GOOG", 50.0);
    Map<String, Double> proportions = Map.of("AAPL", 0.0, "GOOG", 1.0);
    port.rebalance(LocalDate.of(1, 1, 4), prices, proportions);

    assertEquals(Map.of("AAPL", 0.0, "GOOG", 26.0), port.getComposition(LocalDate.of(1, 1, 4)));
  }

  @Test
  public void buySellAndRebalanceWorksForMultipleStocks() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.buyStock("MSFT", LocalDate.of(1, 1, 4), 30.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 5), 5.0);

    Map<String, Double> prices = Map.of("AAPL", 30.0, "GOOG", 50.0, "MSFT", 40.0);
    Map<String, Double> proportions = Map.of("AAPL", 0.3, "GOOG", 0.4, "MSFT", 0.3);
    port.rebalance(LocalDate.of(1, 1, 6), prices, proportions);

    var composition = port.getComposition(LocalDate.of(1, 1, 6));

    double totalValue = composition.get("AAPL") * prices.get("AAPL") + composition.get("GOOG") * prices.get("GOOG") + composition.get("MSFT") * prices.get("MSFT");

    assertEquals((totalValue * 0.3) / prices.get("AAPL"), composition.get("AAPL"), 0.01);
    assertEquals((totalValue * 0.4) / prices.get("GOOG"), composition.get("GOOG"), 0.01);
    assertEquals((totalValue * 0.3) / prices.get("MSFT"), composition.get("MSFT"), 0.01);
  }


  @Test
  public void rebalanceAfterMultipleRebalances() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.buyStock("MSFT", LocalDate.of(1, 1, 4), 30.0);

    Map<String, Double> prices = Map.of("AAPL", 30.0, "GOOG", 50.0, "MSFT", 40.0);
    Map<String, Double> proportions = Map.of("AAPL", 0.2, "GOOG", 0.5, "MSFT", 0.3);
    port.rebalance(LocalDate.of(1, 1, 5), prices, proportions);

    prices = Map.of("AAPL", 35.0, "GOOG", 55.0, "MSFT", 45.0);
    proportions = Map.of("AAPL", 0.3, "GOOG", 0.4, "MSFT", 0.3);
    port.rebalance(LocalDate.of(1, 1, 6), prices, proportions);

    prices = Map.of("AAPL", 40.0, "GOOG", 60.0, "MSFT", 50.0);
    proportions = Map.of("AAPL", 0.25, "GOOG", 0.5, "MSFT", 0.25);
    port.rebalance(LocalDate.of(1, 1, 7), prices, proportions);

    var composition = port.getComposition(LocalDate.of(1, 1, 7));

    double totalValue = composition.get("AAPL") * prices.get("AAPL") + composition.get("GOOG") * prices.get("GOOG") + composition.get("MSFT") * prices.get("MSFT");

    assertEquals(totalValue * 0.25 / prices.get("AAPL"), composition.get("AAPL"), 0.01);
    assertEquals(totalValue * 0.5 / prices.get("GOOG"), composition.get("GOOG"), 0.01);
    assertEquals(totalValue * 0.25 / prices.get("MSFT"), composition.get("MSFT"), 0.01);
  }

  @Test
  public void rebalanceAfterCompletelySellingSomeStocks() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.buyStock("MSFT", LocalDate.of(1, 1, 4), 30.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 5), 10.0);

    Map<String, Double> prices = Map.of("GOOG", 50.0, "MSFT", 40.0);
    Map<String, Double> proportions = Map.of("GOOG", 0.5, "MSFT", 0.5);
    port.rebalance(LocalDate.of(1, 1, 6), prices, proportions);

    var composition = port.getComposition(LocalDate.of(1, 1, 6));

    double totalValue = composition.get("GOOG") * prices.get("GOOG") + composition.get("MSFT") * prices.get("MSFT");

    assertEquals(totalValue * 0.5 / prices.get("GOOG"), composition.get("GOOG"), 0.01);
    assertEquals(totalValue * 0.5 / prices.get("MSFT"), composition.get("MSFT"), 0.01);
  }

  @Test (expected = IllegalArgumentException.class)
  public void rebalanceThrowsExceptionAfterCompletelySellingAStock() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 4), 10.0);

    Map<String, Double> prices = Map.of("AAPL", 30.0, "GOOG", 50.0);
    Map<String, Double> proportions = Map.of("AAPL", 0.5, "GOOG", 0.5);
    port.rebalance(LocalDate.of(1, 1, 5), prices, proportions);
  }

  @Test
  public void rebalanceAfterBuyingSellingAndRebalancingMultipleTimes() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.buyStock("MSFT", LocalDate.of(1, 1, 4), 30.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 5), 10.0);
    port.rebalance(LocalDate.of(1, 1, 6), Map.of("GOOG", 50.0, "MSFT", 40.0), Map.of("GOOG", 0.5, "MSFT", 0.5));

    Map<String, Double> prices = Map.of("GOOG", 55.0, "MSFT", 45.0);
    Map<String, Double> proportions = Map.of("GOOG", 0.6, "MSFT", 0.4);
    port.rebalance(LocalDate.of(1, 1, 7), prices, proportions);

    var composition = port.getComposition(LocalDate.of(1, 1, 7));

    double totalValue = composition.get("GOOG") * prices.get("GOOG") + composition.get("MSFT") * prices.get("MSFT");

    assertEquals(totalValue * 0.6 / prices.get("GOOG"), composition.get("GOOG"), 0.01);
    assertEquals(totalValue * 0.4 / prices.get("MSFT"), composition.get("MSFT"), 0.01);
  }
}