package stock.model;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PortfolioStockModelImplTest extends BasicStockModelTest {
  protected PortfolioStockModel portModel;
  private Path testDir;

  @Override
  public void setup() throws IOException {
    mockDataSource = new MockDataSource();
    testDir = Files.createTempDirectory("testPortfolioSave");
    model = new PortfolioStockModelImpl(mockDataSource, testDir.toString());
  }

  @Before
  public void setupInner() {
    portModel = new PortfolioStockModelImpl(mockDataSource, testDir.toString());
  }

  @Override
  public void testAddStockToPortfolio() {
    try {
      Map<String, Double> newMap = new HashMap<>();
      newMap.put("AAPL", 600.0);
      portModel.createNewPortfolio("Portfolios");
      portModel.addStockToPortfolio("Portfolios", "AAPL", 400, LocalDate.of(1,1,1));
      portModel.addStockToPortfolio("Portfolios", "AAPL", 200, LocalDate.of(1,1,2));
      assertEquals(newMap, portModel.getPortfolioContentsDecimal("Portfolios", LocalDate.of(1,1,
              3)));

      newMap.put("GOOG", 20.0);
      portModel.addStockToPortfolio("Portfolios", "GOOG", 20, LocalDate.of(1,1,3));
      assertEquals(newMap, portModel.getPortfolioContentsDecimal("Portfolios", LocalDate.of(1,1,4)));
    } catch (IOException e) {
      fail("Failed test: I/O Error");
    }
  }

  @Override
  public void testAddStockToPortfolioFailed() {
    portModel.createNewPortfolio("Portfolios");
    portModel.getPortfolioContentsDecimal("Portfolios2", LocalDate.of(1,1,1));
  }

  @Override
  public void testRemoveStockFromPortfolioFaildTicker() {
    try {
      portModel.createNewPortfolio("Portfolio");
      portModel.addStockToPortfolio("Portfolio", "AAPL", 400, LocalDate.of(1,1,1));
      portModel.addStockToPortfolio("Portfolio", "GOOG", 200, LocalDate.of(1,1,2));
      portModel.sellStockFromPortfolio("Portfolio", "A", 200, LocalDate.of(1,1,3));
    } catch (IOException e) {
      fail("Failed test: I/O Error");
    }
  }

  @Override
  public void getPortfolioValue() {
    try {
      portModel.createNewPortfolio("port");
      portModel.addStockToPortfolio("port", "A", 60, LocalDate.of(1,1,1));
      portModel.addStockToPortfolio("port", "GOOG", 500, LocalDate.of(1,1,3));
      LocalDate date = LocalDate.of(2024, 5, 12);
      mockDataSource.setClosingPrice(date, 2);
      mockDataSource.setStockExistsAtDate(date);
      assertEquals(1120.0, portModel.getPortfolioValue("port", date), 0.001);
    } catch (IOException e) {
      fail("Failed to get data from datasource");
    }
  }

  @Override
  public void testGetPortfolioContents() {
    try {
      portModel.createNewPortfolio("firstPortfolio");
      portModel.addStockToPortfolio("firstPortfolio", "GOOG", 30, LocalDate.of(1,1,3));
      Map<String, Double> newMap = new HashMap<>();
      newMap.put("GOOG", 30.0);
      assertEquals(newMap, portModel.getPortfolioContentsDecimal("firstPortfolio",
              LocalDate.of(1,1,5)));
    } catch (IOException e) {
      fail("Failed to get data from datasource");
    }
  }

  @Override
  public void testGetPortfolioContentsFailed() {
    try {
      portModel.createNewPortfolio("firstPortfolio");
      portModel.addStockToPortfolio("firstPortfolio", "GOOG", 30, LocalDate.of(1,1,5));
      portModel.getPortfolioContentsDecimal("port", LocalDate.of(1,5,1));
    } catch (IOException e) {
      fail("Failed to get data from datasource");
    }
  }

  @Override
  public void testRemoveStockFromPortfolioFaildName() {
    try {
      portModel.createNewPortfolio("Portfolio");
      portModel.addStockToPortfolio("Portfolio", "AAPL", 400, LocalDate.of(1,2,3));
      portModel.addStockToPortfolio("Portfolio", "GOOG", 200, LocalDate.of(2,3,4));
      portModel.sellStockFromPortfolio("Portfolio2", "GOOG", 200, LocalDate.of(3,4,5));
    } catch (IOException e) {
      fail("Failed to get data from datasource");
    }

  }

  @Override
  public void testRemoveStockFromPortfolio() {
    try {
      Map<String, Double> newMap = new HashMap<>();
      portModel.createNewPortfolio("Portfolio");
      portModel.addStockToPortfolio("Portfolio", "AAPL", 400, LocalDate.of(1,1,5));
      portModel.addStockToPortfolio("Portfolio", "GOOG", 200, LocalDate.of(1,1,6));
      portModel.sellStockFromPortfolio("Portfolio", "GOOG", 200, LocalDate.of(1,1,8));

      newMap.put("AAPL", 400.0);
      assertEquals(newMap, portModel.getPortfolioContentsDecimal("Portfolio", LocalDate.of(1,1,8)));
    } catch (IOException e) {
      fail("Failed to get data from datasource");
    }
  }

  @Override
  public void getPortfolioValueNoTicker() throws IOException {
    portModel.createNewPortfolio("port");
    portModel.addStockToPortfolio("port", "AAPL", 60, LocalDate.of(1,2,3));
    portModel.addStockToPortfolio("port", "GOOG", 500, LocalDate.of(1,2,5));
    LocalDate date = LocalDate.of(2022, 12, 03);
    mockDataSource.setClosingPrice(date, 2);
    mockDataSource.setStockExistsAtDate(date);
    assertEquals(1120.0, portModel.getPortfolioValue("port", date), 0.001);
  }

  @Test
  public void testGetPortfolioDistributionEmptyPortfolio() throws IOException {
    portModel.createNewPortfolio("emptyPortfolio");

    Map<String, Double> distribution = portModel.getPortfolioDistribution("emptyPortfolio", LocalDate.of(2023, 1, 1));
    assertTrue(distribution.isEmpty());
  }

  @Test
  public void testGetPortfolioDistributionWithTransactions() throws IOException {
    portModel.createNewPortfolio("testPortfolio");

    portModel.addStockToPortfolio("testPortfolio", "AMZN", 10, LocalDate.of(2023, 1, 1));
    portModel.addStockToPortfolio("testPortfolio", "GOOG", 20, LocalDate.of(2023, 1, 2));

    mockDataSource.setStockExistsAtDate(LocalDate.of(2023, 1, 3));
    mockDataSource.setClosingPrice(LocalDate.of(2023, 1, 3), 2500.0);

    Map<String, Double> distribution = portModel.getPortfolioDistribution("testPortfolio", LocalDate.of(2023, 1, 3));

    Map<String, Double> expectedDistribution = new HashMap<>();
    expectedDistribution.put("AMZN", 10 * 2500.0);
    expectedDistribution.put("GOOG", 20 * 2500.0);

    assertEquals(expectedDistribution, distribution);
  }

  @Test
  public void testGetPortfolioDistributionIgnoresFutureTransactions() throws IOException {
    portModel.createNewPortfolio("futureTransactionsPortfolio");

    portModel.addStockToPortfolio("futureTransactionsPortfolio", "A", 10, LocalDate.of(2023, 1, 1));
    portModel.addStockToPortfolio("futureTransactionsPortfolio", "GOOG", 20, LocalDate.of(2023, 1, 2));
    portModel.addStockToPortfolio("futureTransactionsPortfolio", "AMZN", 30, LocalDate.of(2023, 1, 4));

    mockDataSource.setStockExistsAtDate(LocalDate.of(2023, 1, 3));
    mockDataSource.setStockExistsAtDate(LocalDate.of(2023, 1, 3));
    mockDataSource.setClosingPrice(LocalDate.of(2023, 1, 3), 2500.0); // GOOG

    Map<String, Double> distribution = portModel.getPortfolioDistribution("futureTransactionsPortfolio", LocalDate.of(2023, 1, 3));

    Map<String, Double> expectedDistribution = new HashMap<>();
    expectedDistribution.put("A", 10 * 2500.0);
    expectedDistribution.put("GOOG", 20 * 2500.0);

    assertEquals(expectedDistribution, distribution);
  }

  @Test
  public void testGetPortfolioSaves() throws IOException, InterruptedException {
    portModel.createNewPortfolio("testPortfolio");
    portModel.createNewPortfolioSave("testPortfolio");
    Thread.sleep(1500);
    portModel.addStockToPortfolio("testPortfolio", "AAPL", 10, LocalDate.of(2023, 1, 1));
    portModel.createNewPortfolioSave("testPortfolio");

    List<String> saves = portModel.getPortfolioSaves("testPortfolio");

    assertEquals(2, saves.size());
    assertTrue(saves.get(0).startsWith("testPortfolio_"));
    assertTrue(saves.get(1).startsWith("testPortfolio_"));
  }

  @Test
  public void testLoadPortfolioSave() throws IOException {
    portModel.createNewPortfolio("testPortfolio");

    portModel.addStockToPortfolio("testPortfolio", "A", 10, LocalDate.of(2024,5,6));
    portModel.createNewPortfolioSave("testPortfolio");
    portModel.addStockToPortfolio("testPortfolio", "AMZN", 20, LocalDate.of(2024,5,7));
    portModel.createNewPortfolioSave("testPortfolio");

    List<String> saves = portModel.getPortfolioSaves("testPortfolio");
    String latestSave = saves.get(0); // Assuming the latest save is at index 0

    portModel.loadPortfolioSave("testPortfolio", latestSave);

    Map<String, Double> expectedComposition = Map.of("A", 10.0, "AMZN", 20.0);
    assertEquals(expectedComposition, portModel.getPortfolioContentsDecimal("testPortfolio",
            LocalDate.of(2024,5,7)));

    mockDataSource.setClosingPrice(LocalDate.of(2024,5,7), 100.0);
    mockDataSource.setStockExistsAtDate(LocalDate.of(2024,5,7));
    double expectedValue = 10.0 * 100.0 + 20.0 * 100.0;
    assertEquals(expectedValue, portModel.getPortfolioValue("testPortfolio", LocalDate.of(2024, 5
            , 7)), 0.01);
  }

  @Test
  public void testGetPortfolioDistribution() throws IOException {
    portModel.createNewPortfolio("testPortfolio");
    portModel.addStockToPortfolio("testPortfolio", "A", 10, LocalDate.of(2023, 1, 1));
    portModel.addStockToPortfolio("testPortfolio", "AMZN", 20, LocalDate.of(2023, 1, 2));

    mockDataSource.setClosingPrice(LocalDate.of(2023, 1, 2), 2800.0); // GOOG

    Map<String, Double> distribution = portModel.getPortfolioDistribution("testPortfolio", LocalDate.of(2023, 1, 2));

    Map<String, Double> expectedDistribution = Map.of(
            "A",  10 * 2800.0,
            "AMZN", 20 * 2800.0
    );

    assertEquals(expectedDistribution, distribution);
  }

  @Test
  public void testGetPortfolioPerformance() {
    try {
      portModel.createNewPortfolio("performanceTestPortfolio");

      // Add transactions to the portfolio
      portModel.addStockToPortfolio("performanceTestPortfolio", "A", 10, LocalDate.of(2024, 5, 4));
      portModel.addStockToPortfolio("performanceTestPortfolio", "GOOG", 20, LocalDate.of(2024, 5, 6));
      portModel.addStockToPortfolio("performanceTestPortfolio", "AMZN", 30, LocalDate.of(2024, 5, 7));

      // Set closing prices for each date in the mock data source
      mockDataSource.setClosingPrice(LocalDate.of(2024, 5, 6), 200.0);
      mockDataSource.setClosingPrice(LocalDate.of(2024, 5, 7), 300.0);
      mockDataSource.setClosingPrice(LocalDate.of(2024, 5, 8), 400.0);
      mockDataSource.setClosingPrice(LocalDate.of(2024, 5, 9), 500.0);
      mockDataSource.setClosingPrice(LocalDate.of(2024, 5, 12), 600.0);
      mockDataSource.setClosingPrice(LocalDate.of(2024, 5, 15), 700.0);

      // Set stock exists at date
      mockDataSource.setStockExistsAtDate(LocalDate.of(2024, 5, 6));
      mockDataSource.setStockExistsAtDate(LocalDate.of(2024, 5, 7));
      mockDataSource.setStockExistsAtDate(LocalDate.of(2024, 5, 8));
      mockDataSource.setStockExistsAtDate(LocalDate.of(2024, 5, 9));
      mockDataSource.setStockExistsAtDate(LocalDate.of(2024, 5, 12));
      mockDataSource.setStockExistsAtDate(LocalDate.of(2024, 5, 15));

      Map<LocalDate, Double> performance = portModel.getPortfolioPerformance(
              "performanceTestPortfolio", LocalDate.of(2024, 5, 4), LocalDate.of(2024, 5, 16));

      Map<LocalDate, Double> expectedPerformance = new HashMap<>();
      expectedPerformance.put(LocalDate.of(2024, 5, 4), 0.0);
      expectedPerformance.put(LocalDate.of(2024, 5, 5), 0.0);
      expectedPerformance.put(LocalDate.of(2024, 5, 6), 10 * 200.0 + 20 * 200.0);
      expectedPerformance.put(LocalDate.of(2024, 5, 7), 10 * 300.0 + 20 * 300.0 + 30 * 300.0);
      expectedPerformance.put(LocalDate.of(2024, 5, 8), 10 * 400.0 + 20 * 400.0 + 30 * 400.0);
      expectedPerformance.put(LocalDate.of(2024, 5, 9), 10 * 500.0 + 20 * 500.0 + 30 * 500.0);
      expectedPerformance.put(LocalDate.of(2024, 5, 10), 10 * 500.0 + 20 * 500.0 + 30 * 500.0);
      expectedPerformance.put(LocalDate.of(2024, 5, 11), 10 * 500.0 + 20 * 500.0 + 30 * 500.0);
      expectedPerformance.put(LocalDate.of(2024, 5, 12), 10 * 600.0 + 20 * 600.0 + 30 * 600.0);
      expectedPerformance.put(LocalDate.of(2024, 5, 13), 10 * 600.0 + 20 * 600.0 + 30 * 600.0);
      expectedPerformance.put(LocalDate.of(2024, 5, 14), 10 * 600.0 + 20 * 600.0 + 30 * 600.0);
      expectedPerformance.put(LocalDate.of(2024, 5, 15), 10 * 700.0 + 20 * 700.0 + 30 * 700.0);

      assertEquals(expectedPerformance, performance);
    } catch (IOException e) {
      fail("Failed to get data from datasource");
    }
  }
}