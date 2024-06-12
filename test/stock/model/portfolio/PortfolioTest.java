package stock.model.portfolio;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PortfolioTest {

  private Path testDir;

  @Before
  public void setUp() throws IOException {
    testDir = Files.createTempDirectory("testPortfolioSave");
  }

  @After
  public void tearDown() throws IOException {
    // Delete temporary files
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(testDir)) {
      for (Path entry : stream) {
        Files.delete(entry);
      }
    }
    Files.delete(testDir);
  }

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
    assertEquals(Map.of(), port.getComposition(LocalDate.of(1, 1, 1)));
  }

  @Test
  public void getValueWorksForEmptyPort() {
    var port = new Portfolio("portfolio1");
    assertEquals((Double) 0.0, port.getValue(LocalDate.of(1, 1, 1), Map.of("AAPL", 5.0)));
  }

  @Test
  public void getDistributionWorksForEmptyPort() {
    var port = new Portfolio("portfolio1");
    assertEquals(Map.of(), port.getDistribution(LocalDate.of(1, 1, 1), Map.of("AAPL", 155.5)));
  }

  @Test
  public void getCompositionWorksForNonEmptyPort() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("GOOG", LocalDate.of(1, 1, 3), 20.0);
    port.buyStock("MSFT", LocalDate.of(1, 1, 4), 30.0);
    assertEquals(Map.of("AAPL", 10.0, "GOOG", 20.0, "MSFT", 30.0),
            port.getComposition(LocalDate.of(1, 1, 4)));
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
    Map<String, Double> expectedDistribution =
            Map.of("AAPL", 10.0 * 150.0, "GOOG", 20.0 * 250.0, "MSFT", 30.0 * 350.0);
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
    Map<String, Double> expectedDistribution = Map.of("AAPL", 5.0 * 150.0, "GOOG", 20.0 * 250.0);
    assertEquals(expectedDistribution, port.getDistribution(LocalDate.of(1, 1, 4), prices));
  }

  @Test(expected = IllegalArgumentException.class)
  public void sellStockThrowsExceptionWhenStockNotInPort() {
    var port = new Portfolio("portfolio1");
    port.sellStock("AAPL", LocalDate.of(2004, 12, 20), 30.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void sellStockThrowsExceptionWhenNotEnough() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(2003, 12, 20), 25.0);
    port.sellStock("AAPL", LocalDate.of(2004, 12, 20), 30.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void sellStockThrowsExceptionWhenSellingBeforeBought() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(2, 2, 2), 40.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 1), 30.0);
  }

  @Test
  public void buyStockWorksForOne() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(2, 2, 2), 40.0);
    assertEquals(Map.of("AAPL", 40.0), port.getComposition(LocalDate.of(2, 2, 2)));
  }

  @Test
  public void buyStockWorksForMultiple() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 40.0);
    port.buyStock("AAPL", LocalDate.of(1, 1, 4), 20.0);
    port.buyStock("AMZN", LocalDate.of(1, 1, 4), 10.0);

    // This is bought after 1,1,5 so it should not be in portfolio
    port.buyStock("NFLX", LocalDate.of(1, 1, 6), 10.0);
    assertEquals(Map.of("AAPL", 60.0, "AMZN", 10.0), port.getComposition(LocalDate.of(1, 1, 5)));
  }

  @Test
  public void buyWorksForOutOfOrder() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(2, 2, 2), 40.0);
    assertEquals(Map.of("AAPL", 40.0), port.getComposition(LocalDate.of(2, 2, 10)));

    port.buyStock("AAPL", LocalDate.of(2, 2, 3), 50.0);
    assertEquals(Map.of("AAPL", 90.0), port.getComposition(LocalDate.of(2, 2, 10)));
  }

  @Test
  public void sellWorksForExactAmount() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 40.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 4), 40.0);
    assertEquals(Map.of(), port.getComposition(LocalDate.of(1, 1, 4)));
  }

  @Test
  public void sellWorksForInexactAmount() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 40.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 4), 35.0);
    assertEquals(Map.of("AAPL", 5.0), port.getComposition(LocalDate.of(1, 1, 4)));
  }

  @Test
  public void sellWorksForOutOfOrder() {
    var port = new Portfolio("portfolio1");
    port.buyStock("AAPL", LocalDate.of(1, 1, 2), 40.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 4), 35.0);
    port.sellStock("AAPL", LocalDate.of(1, 1, 5), 5.0);
    assertEquals(Map.of(), port.getComposition(LocalDate.of(1, 1, 5)));

    // Buy more stocks in between previous transactions
    port.buyStock("AAPL", LocalDate.of(1, 1, 3), 100.0);
    assertEquals(Map.of("AAPL", 100.0), port.getComposition(LocalDate.of(1, 1, 5)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void rebalanceThrowsExceptionIfProportionsMissingStocks() {
    var port = new Portfolio("portfolio1");
    port.buyStock("A", LocalDate.of(1, 1, 2), 10.0);
    port.buyStock("B", LocalDate.of(1, 1, 3), 20.0);
    Map<String, Double> prices = Map.of("A", 30.0, "B", 20.0);
    Map<String, Double> proportions = Map.of("B", 0.5);
    port.rebalance(LocalDate.of(1, 1, 4), prices, proportions);
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

    double totalValue =
            composition.get("A") * prices.get("A") + composition.get("B") * prices.get("B")
                    + composition.get("C") * prices.get("C");
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

    Map<String, Double> proportions =
            Map.of("AAPL", (10.0 * 30.0) / totalValue, "GOOG", (20.0 * 50.0) / totalValue, "MSFT",
                    (30.0 * 40.0) / totalValue);

    port.rebalance(LocalDate.of(1, 1, 5), prices, proportions);

    assertEquals(Map.of("AAPL", 10.0, "GOOG", 20.0, "MSFT", 30.0),
            port.getComposition(LocalDate.of(1, 1, 5)));
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

    double totalValue =
            composition.get("AAPL") * prices.get("AAPL") + composition.get("GOOG") * prices.get(
                    "GOOG") + composition.get("MSFT") * prices.get("MSFT");

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

    double totalValue =
            composition.get("AAPL") * prices.get("AAPL") + composition.get("GOOG") * prices.get(
                    "GOOG") + composition.get("MSFT") * prices.get("MSFT");

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

    double totalValue =
            composition.get("GOOG") * prices.get("GOOG") + composition.get("MSFT") * prices.get(
                    "MSFT");

    assertEquals(totalValue * 0.5 / prices.get("GOOG"), composition.get("GOOG"), 0.01);
    assertEquals(totalValue * 0.5 / prices.get("MSFT"), composition.get("MSFT"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
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
    port.rebalance(LocalDate.of(1, 1, 6), Map.of("GOOG", 50.0, "MSFT", 40.0),
            Map.of("GOOG", 0.5, "MSFT", 0.5));

    Map<String, Double> prices = Map.of("GOOG", 55.0, "MSFT", 45.0);
    Map<String, Double> proportions = Map.of("GOOG", 0.6, "MSFT", 0.4);
    port.rebalance(LocalDate.of(1, 1, 7), prices, proportions);

    var composition = port.getComposition(LocalDate.of(1, 1, 7));

    double totalValue =
            composition.get("GOOG") * prices.get("GOOG") + composition.get("MSFT") * prices.get(
                    "MSFT");

    assertEquals(totalValue * 0.6 / prices.get("GOOG"), composition.get("GOOG"), 0.01);
    assertEquals(totalValue * 0.4 / prices.get("MSFT"), composition.get("MSFT"), 0.01);
  }

  @Test
  public void createSaveWorksForEmptyPortfolio() throws IOException {
    Portfolio portfolio = new Portfolio("portfolio1");
    portfolio.createSave(testDir.toString(), "testSave");

    // Empty, so there should be nothing in the save file
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(testDir)) {
      for (Path entry : stream) {
        List<String> lines = Files.readAllLines(entry);

        assertTrue(lines.isEmpty());
      }
    }
  }

  @Test
  public void createSaveWorksForPortfolioWithMultipleTransactions() throws IOException {
    Portfolio portfolio = new Portfolio("portfolio3");

    portfolio.buyStock("AAPL", LocalDate.of(2023, 5, 20), 10.0);
    portfolio.buyStock("AMZN", LocalDate.of(2023, 5, 22), 15.0);
    portfolio.buyStock("NFLX", LocalDate.of(2023, 5, 23), 25.0);
    portfolio.sellStock("NFLX", LocalDate.of(2023, 5, 24), 20.0);
    portfolio.rebalance(LocalDate.of(2023, 6, 1),
            Map.of("AAPL", 150.0, "AMZN", 2800.0, "NFLX", 600.0),
            Map.of("AAPL", 0.3, "AMZN", 0.4, "NFLX", 0.3));

    portfolio.createSave(testDir.toString(), "save1");

    // Read from file again
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(testDir)) {
      for (Path entry : stream) {
        List<String> lines = Files.readAllLines(entry);

        assertEquals(List.of("BUY:05/20/2023,10.0,AAPL", "BUY:05/22/2023,15.0,AMZN",
                "BUY:05/23/2023,25.0,NFLX", "SELL:05/24/2023,20.0,NFLX",
                "REBALANCE:06/01/2023,AAPL=>150.0;AMZN=>2800.0;NFLX=>600.0,AAPL=>0.3;AMZN=>0.4;"
                        + "NFLX=>0.3"), lines);
      }
    }

    Map<String, Double> prices = Map.of("AAPL", 150.0, "AMZN", 2800.0, "NFLX", 600.0);
    Double value = portfolio.getValue(LocalDate.of(2023, 6, 1), prices);
    assertEquals(46500.0, value, 0.01);
  }

  @Test
  public void getAllSavesWorksForZeroSaves() throws IOException {
    Portfolio portfolio = new Portfolio("portfolio3");
    assertEquals(List.of(), portfolio.getAllSaves(testDir.toString()));
  }

  @Test
  public void getAllSavesWorksForMultipleSaves() throws IOException {
    Portfolio portfolio = new Portfolio("portfolio3");
    // Create 3 saves. Wait in between so save names dont overlap
    portfolio.buyStock("AAPL", LocalDate.of(2023, 5, 20), 10.0);
    portfolio.createSave(testDir.toString(), "portfolio3_save1");
    assertEquals(List.of("portfolio3_save1"),
            portfolio.getAllSaves(testDir.toString()).stream().sorted()
                    .collect(Collectors.toList()));

    portfolio.buyStock("AMZN", LocalDate.of(2023, 5, 22), 15.0);
    portfolio.createSave(testDir.toString(), "portfolio3_save2");
    assertEquals(List.of("portfolio3_save1", "portfolio3_save2"),
            portfolio.getAllSaves(testDir.toString()).stream().sorted()
                    .collect(Collectors.toList()));

    portfolio.buyStock("NFLX", LocalDate.of(2023, 5, 23), 25.0);
    portfolio.createSave(testDir.toString(), "portfolio3_save3");
    assertEquals(List.of("portfolio3_save1", "portfolio3_save2", "portfolio3_save3"),
            portfolio.getAllSaves(testDir.toString()).stream().sorted()
                    .collect(Collectors.toList()));
  }

  @Test
  public void getAllSavesOnlyGetsSavesOfRequestedPortfolio() throws IOException {
    Portfolio portfolio = new Portfolio("portfolio3");
    portfolio.buyStock("AAPL", LocalDate.of(2023, 5, 20), 10.0);
    portfolio.createSave(testDir.toString(), "portfolio3_save1");
    assertEquals(List.of("portfolio3_save1"), portfolio.getAllSaves(testDir.toString()));

    Portfolio portfolio2 = new Portfolio("otherPortfolio");
    portfolio2.buyStock("NFLX", LocalDate.of(2023, 5, 23), 25.0);
    portfolio2.createSave(testDir.toString(), "otherPortfolio_save1");
    assertEquals(List.of("portfolio3_save1"), portfolio.getAllSaves(testDir.toString()));
    assertEquals(List.of("otherPortfolio_save1"), portfolio2.getAllSaves(testDir.toString()));
  }

  @Test
  public void loadSaveWorksForEmptyPortfolio() throws IOException {
    // Create an empty save file
    String folderName = testDir.toString();
    String fileName = "emptyPortfolio";
    Path folderPath = Paths.get(folderName);
    Path filePath = folderPath.resolve(fileName + ".txt");

    Files.createDirectories(folderPath);
    Files.write(filePath, "".getBytes());

    Portfolio loadedPortfolio = new Portfolio("portfolio1");
    loadedPortfolio.loadSave(folderName, fileName);

    assertEquals(Map.of(), loadedPortfolio.getComposition(LocalDate.of(1, 1, 1)));
    assertEquals((Double) 0.0,
            loadedPortfolio.getValue(LocalDate.of(1, 1, 1), Map.of("AAPL", 5.0)));
  }

  @Test
  public void loadSaveWorksForPortfolioWithMultipleTransactions() throws IOException {
    String folderName = testDir.toString();
    String fileName = "portfolioWithTransactions";
    String content = String.join(System.lineSeparator(), "BUY:05/20/2023,10.0,AAPL",
            "BUY:05/22/2023,15.0,AMZN", "BUY:05/23/2023,25.0,NFLX", "SELL:05/24/2023,20.0,NFLX");
    Path folderPath = Paths.get(folderName);
    Path filePath = folderPath.resolve(fileName + ".txt");

    Files.createDirectories(folderPath);
    Files.write(filePath, content.getBytes());

    Portfolio loadedPortfolio = new Portfolio("portfolio2");
    loadedPortfolio.loadSave(folderName, fileName);

    Map<String, Double> expectedComposition = Map.of("AAPL", 10.0, "AMZN", 15.0, "NFLX", 5.0);
    assertEquals(expectedComposition, loadedPortfolio.getComposition(LocalDate.of(2023, 6, 1)));

    Map<String, Double> prices = Map.of("AAPL", 150.0, "AMZN", 2800.0, "NFLX", 600.0);
    double expectedValue = 10.0 * 150.0 + 15.0 * 2800.0 + 5.0 * 600.0;
    assertEquals(expectedValue, loadedPortfolio.getValue(LocalDate.of(2023, 6, 1), prices), 0.01);
  }

  @Test
  public void loadSaveWorksForPortfolioWithRebalance() throws IOException {
    String folderName = testDir.toString();
    String fileName = "portfolioWithRebalance";
    String content = String.join(System.lineSeparator(), "BUY:05/20/2023,10.0,AAPL",
            "BUY:05/22/2023,15.0,GOOG",
            "REBALANCE:06/01/2023,AAPL=>400.0;GOOG=>1000.0,AAPL=>0.5;GOOG=>0.5");
    Path folderPath = Paths.get(folderName);
    Path filePath = folderPath.resolve(fileName + ".txt");

    Files.createDirectories(folderPath);
    Files.write(filePath, content.getBytes());

    Portfolio loadedPortfolio = new Portfolio("portfolio3");
    loadedPortfolio.loadSave(folderName, fileName);

    Map<String, Double> expectedComposition = Map.of("GOOG", 9.5, "AAPL", 23.75);
    assertEquals(expectedComposition, loadedPortfolio.getComposition(LocalDate.of(2023, 6, 1)));

    Map<String, Double> prices = Map.of("AAPL", 400.0, "GOOG", 1000.0);
    double expectedValue = 23.75 * 400.0 + 9.5 * 1000.0;
    assertEquals(expectedValue, loadedPortfolio.getValue(LocalDate.of(2023, 6, 1), prices), 0.01);
  }

  @Test
  public void saveAndLoadSameFileWorks() throws IOException {
    Portfolio portfolio = new Portfolio("portfolio1");

    portfolio.buyStock("AAPL", LocalDate.of(2023, 5, 20), 10.0);
    portfolio.buyStock("GOOG", LocalDate.of(2023, 5, 22), 15.0);
    portfolio.sellStock("AAPL", LocalDate.of(2023, 5, 24), 5.0);

    Map<String, Double> expectedComposition = Map.of("AAPL", 5.0, "GOOG", 15.0);
    assertEquals(expectedComposition, portfolio.getComposition(LocalDate.of(2023, 5, 24)));

    Map<String, Double> prices = Map.of("AAPL", 150.0, "GOOG", 2800.0);
    double expectedValue = 5.0 * 150.0 + 15.0 * 2800.0;
    assertEquals(expectedValue, portfolio.getValue(LocalDate.of(2023, 5, 24), prices), 0.01);

    String folderName = testDir.toString();
    String fileName = "portfolioSave";
    portfolio.createSave(folderName, fileName);

    Portfolio loadedPortfolio = new Portfolio("portfolio1");
    loadedPortfolio.loadSave(folderName, fileName);

    assertEquals(expectedComposition, loadedPortfolio.getComposition(LocalDate.of(2023, 5, 24)));
    assertEquals(expectedValue, loadedPortfolio.getValue(LocalDate.of(2023, 5, 24), prices), 0.01);
  }

  @Test
  public void getValueIgnoresFutureTransactions() {
    Portfolio portfolio = new Portfolio("portfolio1");

    portfolio.buyStock("AAPL", LocalDate.of(2023, 6, 1), 10.0);
    portfolio.sellStock("AAPL", LocalDate.of(2023, 6, 2), 5.0);
    portfolio.buyStock("AAPL", LocalDate.of(2023, 6, 5), 20.0);

    Map<String, Double> prices = Map.of("AAPL", 150.0);

    double expectedValue = 5.0 * 150.0;
    assertEquals(expectedValue, portfolio.getValue(LocalDate.of(2023, 6, 3), prices), 0.01);
  }

  @Test
  public void getCompositionIgnoresFutureTransactions() {
    Portfolio portfolio = new Portfolio("portfolio1");

    portfolio.buyStock("AAPL", LocalDate.of(2023, 6, 1), 10.0);
    portfolio.sellStock("AAPL", LocalDate.of(2023, 6, 2), 5.0);
    portfolio.buyStock("AAPL", LocalDate.of(2023, 6, 5), 20.0);

    Map<String, Double> expectedComposition = Map.of("AAPL", 5.0);
    assertEquals(expectedComposition, portfolio.getComposition(LocalDate.of(2023, 6, 3)));
  }

  @Test
  public void getPortfolioDistributionIgnoresFutureTransactions() {
    Portfolio portfolio = new Portfolio("portfolio1");

    portfolio.buyStock("AAPL", LocalDate.of(2023, 6, 1), 10.0);
    portfolio.sellStock("AAPL", LocalDate.of(2023, 6, 2), 5.0);
    portfolio.buyStock("AAPL", LocalDate.of(2023, 6, 5), 20.0);

    Map<String, Double> prices = Map.of("AAPL", 150.0);

    Map<String, Double> expectedDistribution = Map.of("AAPL", 5.0 * 150.0);
    assertEquals(expectedDistribution, portfolio.getDistribution(LocalDate.of(2023, 6, 3), prices));
  }

  @Test
  public void multipleTransactionsWithDifferentDates() {
    Portfolio portfolio = new Portfolio("portfolio2");

    portfolio.buyStock("AAPL", LocalDate.of(2023, 6, 1), 10.0);
    portfolio.buyStock("GOOG", LocalDate.of(2023, 6, 2), 15.0);
    portfolio.sellStock("AAPL", LocalDate.of(2023, 6, 3), 5.0);
    portfolio.buyStock("AAPL", LocalDate.of(2023, 6, 5), 20.0);
    portfolio.sellStock("GOOG", LocalDate.of(2023, 6, 6), 10.0);

    Map<String, Double> prices = Map.of("AAPL", 150.0, "GOOG", 2800.0);

    double expectedValue = 5.0 * 150.0 + 15.0 * 2800.0;
    assertEquals(expectedValue, portfolio.getValue(LocalDate.of(2023, 6, 4), prices), 0.01);

    Map<String, Double> expectedComposition = Map.of("AAPL", 5.0, "GOOG", 15.0);
    assertEquals(expectedComposition, portfolio.getComposition(LocalDate.of(2023, 6, 4)));

    Map<String, Double> expectedDistribution = Map.of("AAPL", 5.0 * 150.0, "GOOG", 15.0 * 2800.0);
    assertEquals(expectedDistribution, portfolio.getDistribution(LocalDate.of(2023, 6, 4), prices));
  }

  @Test
  public void compositionWithNoTransactionsBeforeDate() {
    Portfolio portfolio = new Portfolio("portfolio3");

    portfolio.buyStock("AAPL", LocalDate.of(2023, 6, 10), 10.0);

    assertEquals(Map.of(), portfolio.getComposition(LocalDate.of(2023, 6, 1)));
  }

}