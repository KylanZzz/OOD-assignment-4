package stock.model;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


/**
 * Tests for the BasicStockModel class using a MockDataSource to simulate the DataSource interface.
 * This class ensures that BasicStockModel behaves as expected when interacting with stock data,
 * focusing on functionalities like calculating gains over time, checking stock existence,
 * and managing portfolios.
 */

public class BasicStockModelTest {
  protected MockDataSource mockDataSource;
  protected StockModel model;

  /**
   * Sets up the environment for each test case.
   * This method initializes the mock data source,
   * and the BasicStockModel with this mock data source.
   */
  @Before
  public void setup() throws IOException {
    mockDataSource = new MockDataSource();
    model = new BasicStockModel(mockDataSource);
  }

  @Test
  public void testGetGainOverTime() throws IOException {
    LocalDate startDate = LocalDate.of(2024, 5, 6);
    LocalDate endDate = LocalDate.of(2024, 5, 9);
    String ticker = "GOOG";

    mockDataSource.setStockExistsAtDate(startDate);
    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setClosingPrice(startDate, 150.0);
    mockDataSource.setClosingPrice(endDate, 160.0);

    double gain = model.getGainOverTime(startDate, endDate, ticker);

    assertEquals(10.0, gain, 0.001);
  }

  @Test(expected = IOException.class)
  public void testGetGainOverTimeFailedWIthNoTicker() throws IOException {
    LocalDate startDate = LocalDate.of(2024, 05, 04);
    LocalDate endDate = LocalDate.of(2024, 06, 04);
    String ticker = "AAPL";

    mockDataSource.setStockExistsAtDate(startDate);
    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setClosingPrice(startDate, 150.0);
    mockDataSource.setClosingPrice(endDate, 160.0);

    double gain = model.getGainOverTime(startDate, endDate, ticker);

    assertEquals(10.0, gain, 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetGainOverTimeFailed() throws IOException {
    LocalDate startDate = LocalDate.of(2023, 6, 1);
    LocalDate endDate = LocalDate.of(2023, 5, 1);
    model.getGainOverTime(startDate, endDate, "A");
  }


  @Test
  public void testGetGainOverTimeIntermediateDate() throws IOException {
    LocalDate startDate = LocalDate.of(2024, 5, 1);
    LocalDate intermediateDate = LocalDate.of(2024, 5, 6);
    LocalDate endDate = LocalDate.of(2024, 5, 15);
    String ticker = "GOOG";

    mockDataSource.setStockExistsAtDate(startDate);
    mockDataSource.setStockExistsAtDate(startDate.plusDays(1));

    mockDataSource.setStockExistsAtDate(intermediateDate);
    mockDataSource.setStockExistsAtDate(intermediateDate.plusDays(1));
    mockDataSource.setStockExistsAtDate(endDate);

    mockDataSource.setClosingPrice(intermediateDate, 150.0);
    mockDataSource.setClosingPrice(intermediateDate.plusDays(1), 142.0);
    mockDataSource.setClosingPrice(endDate, 159.0);

    double gain = model.getGainOverTime(startDate, endDate, ticker);
    assertEquals(9.0, gain, 0.001);
  }

  @Test(expected = IOException.class)
  public void testGetGainOverTimeNoTicker() throws IOException {
    LocalDate startDate = LocalDate.of(2024, 03, 04);
    LocalDate endDate = LocalDate.of(2024, 04, 04);
    String ticker = "AAPL";

    mockDataSource.setStockExistsAtDate(startDate);
    mockDataSource.setStockExistsAtDate(endDate);

    double gain = model.getGainOverTime(startDate, endDate, ticker);
  }


  @Test
  public void testGetGainOverTimeNoValidDate() throws IOException {
    LocalDate startDate = LocalDate.of(2024, 03, 04);
    LocalDate endDate = LocalDate.of(2024, 04, 04);
    String ticker = "AMZN";

    mockDataSource.setStockExistsAtDate(startDate);
    mockDataSource.setStockExistsAtDate(endDate);


    double gain = model.getGainOverTime(startDate, endDate, ticker);

    assertEquals(0.0, gain, 0.001);
  }

  @Test
  public void testGetGainOverTimeValidAtEnd() throws IOException {
    LocalDate startDate = LocalDate.of(2024, 05, 04);
    LocalDate endDate = LocalDate.of(2024, 06, 04);
    String ticker = "AMZN";

    mockDataSource.setStockExistsAtDate(startDate);
    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setClosingPrice(startDate, 150.0);
    mockDataSource.setClosingPrice(endDate, 160.0);

    double gain = model.getGainOverTime(startDate, endDate, ticker);

    assertEquals(0, gain, 0.001);
  }

  @Test
  public void testGetGainOverTimeValidAtEnd2() throws IOException {
    LocalDate startDate = LocalDate.of(2024, 05, 3);
    LocalDate someday = LocalDate.of(2024, 05, 7);
    LocalDate someday2 = LocalDate.of(2024, 05, 8);
    LocalDate someday3 = LocalDate.of(2024, 05, 9);

    LocalDate endDate = LocalDate.of(2024, 06, 4);
    String ticker = "GOOG";

    mockDataSource.setStockExistsAtDate(startDate);
    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setStockExistsAtDate(someday);
    mockDataSource.setStockExistsAtDate(someday2);
    mockDataSource.setStockExistsAtDate(someday3);

    mockDataSource.setClosingPrice(startDate, 150.0);
    mockDataSource.setClosingPrice(someday, 100.0);
    mockDataSource.setClosingPrice(someday2, 110.0);
    mockDataSource.setClosingPrice(someday3, 105.0);

    mockDataSource.setClosingPrice(endDate, 160.0);

    double gain = model.getGainOverTime(startDate, endDate, ticker);

    assertEquals(5, gain, 0.001);
  }

  @Test
  public void testGetMovingDayAverage() throws IOException {
    LocalDate endDate = LocalDate.of(2024, 5, 9);
    int days = 3;
    String ticker = "A";

    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setClosingPrice(endDate, 10.0);

    LocalDate first = endDate.minusDays(1);
    mockDataSource.setStockExistsAtDate(first);
    mockDataSource.setClosingPrice(first, 20.0);

    LocalDate second = first.minusDays(1);
    mockDataSource.setStockExistsAtDate(second);
    mockDataSource.setClosingPrice(second, 20.0);

    double average = model.getMovingDayAverage(endDate, days, ticker);

    assertEquals(16.67, average, 0.1);
  }

  @Test(expected = IOException.class)
  public void testGetMovingDayAveNoTicker() throws IOException {
    LocalDate endDate = LocalDate.of(2022, 12, 03);
    int days = 3;
    String ticker = "Asad";

    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setClosingPrice(endDate, 10.0);

    LocalDate first = endDate.minusDays(1);
    mockDataSource.setStockExistsAtDate(first);
    mockDataSource.setClosingPrice(first, 20.0);

    LocalDate second = first.minusDays(1);
    mockDataSource.setStockExistsAtDate(second);
    mockDataSource.setClosingPrice(second, 20.0);

    double average = model.getMovingDayAverage(endDate, days, ticker);
  }

  @Test
  public void testGetMovingDayAverageNoData() throws IOException {
    LocalDate endDate = LocalDate.of(2022, 12, 03);
    int days = 3;
    String ticker = "A";

    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setClosingPrice(endDate, 10.0);

    LocalDate first = endDate.minusDays(1);
    mockDataSource.setStockExistsAtDate(first);
    mockDataSource.setClosingPrice(first, 20.0);

    LocalDate second = first.minusDays(1);
    mockDataSource.setStockExistsAtDate(second);
    mockDataSource.setClosingPrice(second, 20.0);

    double average = model.getMovingDayAverage(endDate, days, ticker);

    assertEquals(0, average, 0.1);
  }

  @Test
  public void testGetCrossover() throws IOException {
    LocalDate endDate = LocalDate.of(2024, 5, 9);
    int days = 3;
    String ticker = "A";

    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setClosingPrice(endDate, 10.0);

    LocalDate first = endDate.minusDays(1);
    mockDataSource.setStockExistsAtDate(first);
    mockDataSource.setClosingPrice(first, 20.0);

    LocalDate second = first.minusDays(1);
    mockDataSource.setStockExistsAtDate(second);
    mockDataSource.setClosingPrice(second, 20.0);

    List<LocalDate> crossOvers = new ArrayList<>();

    List<LocalDate> actualCrossOvers = model.getCrossover(endDate, days, ticker);

    crossOvers.add(first);
    crossOvers.add(second);

    for (int i = 0; i < crossOvers.size(); i++) {
      assertEquals(crossOvers.get(i), actualCrossOvers.get(i));
    }
  }

  @Test(expected = IOException.class)
  public void testGetCrossoverNoTicker() throws IOException {
    LocalDate endDate = LocalDate.of(2024, 5, 9);
    int days = 3;
    String ticker = "AA";

    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setClosingPrice(endDate, 10.0);

    LocalDate first = endDate.minusDays(1);
    mockDataSource.setStockExistsAtDate(first);
    mockDataSource.setClosingPrice(first, 20.0);

    LocalDate second = first.minusDays(1);
    mockDataSource.setStockExistsAtDate(second);
    mockDataSource.setClosingPrice(second, 20.0);

    List<LocalDate> actualCrossOvers = model.getCrossover(endDate, days, ticker);


  }

  @Test
  public void testGetCrossoverNoCrossOver() throws IOException {
    LocalDate endDate = LocalDate.of(2022, 10, 3);
    int days = 2;
    String ticker = "A";

    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setClosingPrice(endDate, 10.0);

    LocalDate first = endDate.minusDays(1);
    mockDataSource.setStockExistsAtDate(first);
    mockDataSource.setClosingPrice(first, 20.0);

    List<LocalDate> actualCrossOvers = model.getCrossover(endDate, days, ticker);

    assertEquals(0, actualCrossOvers.size());
  }

  @Test
  public void testCreateNewPortolio() {
    assertEquals(Collections.emptyList(), model.getPortfolios());

    model.createNewPortfolio("newPortfolio");
    model.createNewPortfolio("");

    var out = model.getPortfolios();
    Collections.sort(out);

    assertEquals(List.of("", "newPortfolio"), out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewPortolioFailed() {
    assertEquals(Collections.emptyList(), model.getPortfolios());

    model.createNewPortfolio("newPortfolio");
    model.createNewPortfolio("newPortfolio");
  }

  @Test
  public void testDeletePortfolio() {
    model.createNewPortfolio("newPortfolio");
    model.createNewPortfolio("otherPortfolio");
    assertEquals(List.of("newPortfolio", "otherPortfolio"), model.getPortfolios());
    model.deletePortfolio("newPortfolio");
    assertEquals(List.of("otherPortfolio"), model.getPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeletePortfolioNoSuchName() {
    model.createNewPortfolio("newPortfolio");
    model.createNewPortfolio("otherPortfolio");
    assertEquals(List.of("newPortfolio", "otherPortfolio"), model.getPortfolios());
    model.deletePortfolio("asdf");
    assertEquals(List.of("newPortfolio", "otherPortfolio"), model.getPortfolios());
  }

  @Test
  public void testRenamePortfolio() {
    model.createNewPortfolio("newPortfolio");
    assertEquals(List.of("newPortfolio"), model.getPortfolios());
    model.renamePortfolio("newPortfolio", "thisPortfolio");
    assertEquals(List.of("thisPortfolio"), model.getPortfolios());
  }

  @Test
  public void testGetPortfolioContents() {
    model.createNewPortfolio("firstPortfolio");
    model.addStockToPortfolio("firstPortfolio", "GOOG", 30);
    Map<String, Integer> newMap = new HashMap<>();
    newMap.put("GOOG", 30);
    assertEquals(newMap, model.getPortfolioContents("firstPortfolio"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioContentsFailed() {
    model.createNewPortfolio("firstPortfolio");
    model.addStockToPortfolio("firstPortfolio", "GOOG", 30);
    model.getPortfolioContents("port");
  }

  @Test
  public void testGetPortfolios() {
    model.createNewPortfolio("first");
    model.createNewPortfolio("second");
    model.createNewPortfolio("third");
    var out = model.getPortfolios();
    Collections.sort(out);
    assertEquals(List.of("first", "second", "third"), out);
  }

  @Test
  public void testGetPortfoliosEmpty() {
    assertEquals(List.of(), model.getPortfolios());
  }

  @Test
  public void getPortfolioValue() throws IOException {
    model.createNewPortfolio("port");
    model.addStockToPortfolio("port", "A", 60);
    model.addStockToPortfolio("port", "GOOG", 500);
    LocalDate date = LocalDate.of(2024, 5, 12);
    mockDataSource.setClosingPrice(date, 2);
    mockDataSource.setStockExistsAtDate(date);
    assertEquals(1120.0, model.getPortfolioValue("port", date), 0.001);
  }

  @Test(expected = IOException.class)
  public void getPortfolioValueNoTicker() throws IOException {
    model.createNewPortfolio("port");
    model.addStockToPortfolio("port", "AAPL", 60);
    model.addStockToPortfolio("port", "GOOG", 500);
    LocalDate date = LocalDate.of(2022, 12, 03);
    mockDataSource.setClosingPrice(date, 2);
    mockDataSource.setStockExistsAtDate(date);
    assertEquals(1120.0, model.getPortfolioValue("port", date), 0.001);
  }

  @Test
  public void testAddStockToPortfolio() {
    Map<String, Integer> newMap = new HashMap<>();
    newMap.put("AAPL", 600);
    model.createNewPortfolio("Portfolios");
    model.addStockToPortfolio("Portfolios", "AAPL", 400);
    model.addStockToPortfolio("Portfolios", "AAPL", 200);
    assertEquals(newMap, model.getPortfolioContents("Portfolios"));

    newMap.put("GOOG", 20);
    model.addStockToPortfolio("Portfolios", "GOOG", 20);
    assertEquals(newMap, model.getPortfolioContents("Portfolios"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStockToPortfolioFailed() {
    model.createNewPortfolio("Portfolios");
    model.getPortfolioContents("Portfolios2");
  }

  @Test
  public void testRemoveStockFromPortfolio() {
    Map<String, Integer> newMap = new HashMap<>();
    model.createNewPortfolio("Portfolio");
    model.addStockToPortfolio("Portfolio", "AAPL", 400);
    model.addStockToPortfolio("Portfolio", "GOOG", 200);
    model.removeStockFromPortfolio("Portfolio", "GOOG");

    newMap.put("AAPL", 400);
    assertEquals(newMap, model.getPortfolioContents("Portfolio"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveStockFromPortfolioFaildName() {
    Map<String, Integer> newMap = new HashMap<>();
    model.createNewPortfolio("Portfolio");
    model.addStockToPortfolio("Portfolio", "AAPL", 400);
    model.addStockToPortfolio("Portfolio", "GOOG", 200);
    model.removeStockFromPortfolio("Portfolio2", "GOOG");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveStockFromPortfolioFaildTicker() {
    Map<String, Integer> newMap = new HashMap<>();
    model.createNewPortfolio("Portfolio");
    model.addStockToPortfolio("Portfolio", "AAPL", 400);
    model.addStockToPortfolio("Portfolio", "GOOG", 200);
    model.removeStockFromPortfolio("Portfolio", "A");
  }

  @Test
  public void testStockExistsNoTicker() throws IOException {
    String ticker = "AAPL";

    LocalDate startDate = LocalDate.of(2024, 05, 04);
    LocalDate endDate = LocalDate.of(2024, 06, 04);

    mockDataSource.setStockExistsAtDate(startDate);
    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setClosingPrice(startDate, 150.0);
    mockDataSource.setClosingPrice(endDate, 160.0);
    assertEquals(false, model.stockExists(ticker));
  }

  @Test
  public void testStockExists() throws IOException {
    String ticker = "AMZN";

    LocalDate startDate = LocalDate.of(2024, 05, 04);
    LocalDate endDate = LocalDate.of(2024, 06, 04);

    mockDataSource.setStockExistsAtDate(startDate);
    mockDataSource.setStockExistsAtDate(endDate);
    mockDataSource.setClosingPrice(startDate, 150.0);
    mockDataSource.setClosingPrice(endDate, 160.0);
    assertEquals(true, model.stockExists(ticker));
  }

}