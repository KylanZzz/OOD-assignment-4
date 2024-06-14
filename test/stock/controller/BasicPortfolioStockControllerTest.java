package stock.controller;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.BasicStockView;
import stock.view.PortfolioStockView;
import stock.view.StockView;

import static org.junit.Assert.*;

public class BasicPortfolioStockControllerTest extends BasicStockControllerTest {
  protected final class MockPortfolioModel implements PortfolioStockModel {
    private StringBuilder log;
    private boolean throwIOException;
    private MockModel mockModelHelper;

    public MockPortfolioModel(StringBuilder log, boolean throwIOException) {
      this.log = log;
      this.throwIOException = throwIOException;
      this.mockModelHelper = new MockModel(log, throwIOException);

    }

    @Override
    public Map<String, Double> getPortfolioContentsDecimal(String name, LocalDate date)
            throws IllegalArgumentException {

      return Map.of("AAPL", 5.5, "AMZN", 10.5, "NFLX", 15.5);
    }

    @Override
    public void addStockToPortfolio(String name, String ticker, int shares, LocalDate date)
            throws IOException, IllegalArgumentException {
      if (throwIOException) {
        log.append("addStockToPortfolioIOException\n");
      }
      log.append("addStockToPortfolio").append(name).append(ticker).append(shares).append(date).append("\n");
    }


    //similar test in other controller test
    @Override
    public void sellStockFromPortfolio(String name, String ticker, int shares, LocalDate date)
            throws IOException, IllegalArgumentException {
      if (throwIOException) {
        log.append("sellStockFromPortfolioIOException\n");
      }
      log.append("sellStockFromPortfolio").append(name).append(ticker).append(shares).append(date).append("\n");

    }


    @Override
    public double getGainOverTime(LocalDate startDate, LocalDate endDate, String ticker)
            throws IOException {
      if (throwIOException) {
        log.append("getGainOverTimeIOException\n");
        throw new IOException("gainIOExceptionMessage");
      }
      return 100;
    }


    @Override
    public double getMovingDayAverage(LocalDate endDate, int days, String ticker)
            throws IOException {
      if (throwIOException) {
        log.append("getMovingDayAverageIOException\n");
        throw new IOException("averageIOExceptionMessage");
      }
      return 200;
    }

    @Override
    public List<LocalDate> getCrossover(LocalDate endDate, int days, String ticker)
            throws IOException {
      if (throwIOException) {
        log.append("getCrossoverIOException\n");
        throw new IOException("crossoverIOExceptionMessage");
      }
      log.append("getCrossover").append(endDate).append(days).append(ticker).append("\n");
      return List.of(LocalDate.of(1, 1, 1), LocalDate.of(2, 2, 2));
    }

    @Override
    public void createNewPortfolio(String name) {
//      log.append("createNewPortfolio").append(name).append("\n");
      mockModelHelper.createNewPortfolio(name);
    }

    @Override
    public void deletePortfolio(String name) {
//      log.append("deletePortfolio").append(name).append("\n");
      mockModelHelper.deletePortfolio(name);

    }

    @Override
    public void renamePortfolio(String oldName, String newName) {
//      log.append("renamePortfolio").append(oldName).append(newName).append("\n");
      mockModelHelper.renamePortfolio(oldName, newName);

    }

    @Override
    public List<String> getPortfolios() {
      return List.of("S&P500", "NASDAQ");
    }

    @Override
    public double getPortfolioValue(String name, LocalDate date) throws IOException, IllegalArgumentException {
      if (throwIOException) {
        log.append("getPortfolioValueIOException\n");
        throw new IOException("portfolioValueIOExceptionMessage");
      }
      return 400;
    }

    @Override
    public boolean stockExists(String ticker) throws IOException {
      return List.of("AAPL", "NFLX", "AMZN", "TSLA").contains(ticker);
    }

    @Override
    public Map<String, Double> getPortfolioDistribution(String name, LocalDate date)
            throws IOException, IllegalArgumentException {
      if (throwIOException) {
        log.append("getPortfolioDistributionIOException\n");
        throw new IOException("getPortfolioDistributionIOExceptionMessage");
      }
      log.append("getPortfolioDistribution").append(name).append(date).append("\n");
      return Map.of("AMZN", 0.25, "GOOG", 0.25, "AAPL", 0.25);
    }

    @Override
    public List<String> getPortfolioSaves(String name) throws IllegalArgumentException, IOException {
      if (throwIOException) {
        log.append("getPortfolioSavesIOException\n");
        throw new IOException("getPortfolioSavesIOExceptionMessage");
      }
      log.append("getPortfolioSaves").append(name).append("\n");
      return List.of("AA", "BB", "CC");
    }

    @Override
    public void loadPortfolioSave(String name, String fileSaveName) throws IOException, IllegalArgumentException {
      log.append("loadPortfolioSave").append(name).append(fileSaveName).append("\n");
    }

    @Override
    public void createNewPortfolioSave(String name) throws IOException, IllegalArgumentException {
      log.append("createNewPortfolioSave").append(name).append("\n");

    }

    @Override
    public void rebalancePortfolio(String name, LocalDate date, Map<String, Double> proportions) throws IOException, IllegalArgumentException {
      log.append("rebalancePortfolio").append(name).append(date).append(proportions).append("\n");
    }

    @Override
    public Map<LocalDate, Double> getPortfolioPerformance(String name, LocalDate startDate, LocalDate endDate) throws IllegalArgumentException, IOException {
      if (throwIOException) {
        log.append("getPortfolioPerformanceIOException\n");
        throw new IOException("getPortfolioPerformanceIOExceptionMessage");
      }
      log.append("getPortfolioPerformance").append(name).append(startDate).append(endDate).append("\n");
      return Map.of(LocalDate.of(2024, 1, 1), 10.0, LocalDate.of(2024, 1, 2),
              15.0, LocalDate.of(2024, 1, 3), 20.0, LocalDate.of(2024, 1, 3), 15.0,
              LocalDate.of(2024, 1, 4), 25.0, LocalDate.of(2024, 1, 5), 5.0);
    }
  }

  protected final class MockPortfolioView implements PortfolioStockView {

    private StringBuilder log;
    private MockView mockViewHelper;

    public MockPortfolioView(StringBuilder log) {
      this.log = log;
      this.mockViewHelper = new MockView(log);

    }

    @Override
    public void printManagePortfolioDouble(Map<String, Double> stocks, String name) {
      log.append("printManagePortfolioDouble").append(name);
      List<String> list = new ArrayList<>();
      for (var s : stocks.entrySet()) {
        list.add(s.getKey());
        list.add(Double.toString(s.getValue()));
      }
      Collections.sort(list);
      Collections.reverse(list);
      list.forEach(it -> log.append(it));
      log.append("\n");
    }

    @Override
    public void printDistribution(Map<String, Double> stocks, String name, LocalDate date) {
      log.append("printDistribution").append(name);
      List<String> list = new ArrayList<>();
      for (var s : stocks.entrySet()) {
        list.add(s.getKey());
        list.add(Double.toString(s.getValue()));
      }
      Collections.sort(list);
      Collections.reverse(list);
      list.forEach(it -> log.append(it));
      log.append("\n");
    }

    @Override
    public void printFileSaveName(List<String> fileName) {
      log.append("printFileSaveName");
      for (var port : fileName) {
        log.append(port);
      }
      log.append("\n");
    }

    @Override
    public void printPortfolioPerformance(Map<LocalDate, Double> performance, LocalDate startDate, LocalDate endDate) {
      log.append("printPortfolioPerformance").append(startDate).append(endDate);
      List<String> list = new ArrayList<>();
      for (var s : performance.entrySet()) {
        list.add(String.valueOf(s.getKey()));
        list.add(Double.toString(s.getValue()));
      }
      Collections.sort(list);
      Collections.reverse(list);
      list.forEach(it -> log.append(it));
      log.append("\n");

    }

    @Override
    public void printPortfolioOption() {

    }

    @Override
    public void printWelcomeScreen() {
      mockViewHelper.printWelcomeScreen();
    }

    @Override
    public void printMainMenu() {
      mockViewHelper.printMainMenu();
    }

    @Override
    public void printMessage(String message) {
      mockViewHelper.printMessage(message);
    }

    @Override
    public void printViewPortfolios(List<String> portfolios) {
      mockViewHelper.printViewPortfolios(portfolios);
    }

    @Override
    public void printManagePortfolio(Map<String, Integer> stocks, String name) {
      mockViewHelper.printManagePortfolio(stocks, name);
    }

    @Override
    public void printXDayCrossovers(String ticker, LocalDate date, int days, List<LocalDate> dates) {
      mockViewHelper.printXDayCrossovers(ticker, date, days, dates);
    }

    @Override
    public void printStockGain(String ticker, LocalDate startDate, LocalDate endDate, double gain) {
      mockViewHelper.printStockGain(ticker, startDate, endDate, gain);
    }

    @Override
    public void printStockAverage(String ticker, LocalDate endDate, int days, double average) {
      mockViewHelper.printStockAverage(ticker, endDate, days, average);
    }
  }

  private boolean runTest(boolean throwException, Interaction... interactions) {
    StringBuilder expectedViewLog = new StringBuilder();
    StringBuilder expectedModelLog = new StringBuilder();
    StringBuilder fakeInput = new StringBuilder();

    for (var inter : interactions) {
      inter.apply(fakeInput, expectedViewLog, expectedModelLog);
    }

    Reader in = new StringReader(fakeInput.toString());
    StringBuilder viewLog = new StringBuilder();
    StringBuilder modelLog = new StringBuilder();
    PortfolioStockView view = new MockPortfolioView(viewLog);
    PortfolioStockModel model = new MockPortfolioModel(modelLog, throwException);
    StockController controller = new BasicPortfolioStockController(view, model, in);
    controller.run();

    assertEquals(expectedViewLog.toString(), viewLog.toString());
    assertEquals(expectedModelLog.toString(), modelLog.toString());

    return expectedViewLog.toString().equals(viewLog.toString()) && expectedModelLog.toString()
            .equals(modelLog.toString());
  }

  String performancePrompt = "Please enter the name of the portfolio "
          + "that you would like to see the performance: ";

  String rebalancePortfolioNamePrompt = "Please enter the name of the portfolio "
          + "that you would like to rebalance: ";


  String invalidDate = "printMessageInvalid date: Please enter a valid date.";
  String invalidDateFormat =
          "printMessageIncorrect format: Please enter the date in the format "
                  + "MM/DD/YYYY.";
  String invalidInputInteger = "printMessageInvalid input: not an integer, please try again.";
  String invalidInputDateOrder = "printMessageInvalid input: The end date must be after the "
          + "start" + " date.";
  String startDatePrompt = "printMessagePlease enter the starting date (inclusive) in the format "
          + "MM/DD/YYYY:";
  String endDatePrompt = "printMessagePlease enter the ending date (inclusive) in the format "
          + "MM/DD/YYYY:";
  String tickerIncorrect = "printMessageThat stock does not exist! Please try again.";




}