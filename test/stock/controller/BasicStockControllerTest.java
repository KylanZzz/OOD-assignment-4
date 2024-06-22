package stock.controller;

import org.junit.Test;

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
import stock.view.BasicMenuOptions;
import stock.view.PortfolioStockView;
import stock.view.StockView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static stock.controller.Interactions.inputs;
import static stock.controller.Interactions.modelLog;
import static stock.controller.Interactions.prints;

/**
 * Unit test for the BasicStockController class that ensures
 * the controller correctly passes information from the model
 * to the view and handles user input properly.
 */
public class BasicStockControllerTest {

  protected static final class MockModel implements PortfolioStockModel {
    private StringBuilder log;
    private boolean throwIOException;

    public MockModel(StringBuilder log, boolean throwIOException) {
      this.log = log;
      this.throwIOException = throwIOException;
    }

    // Dummy data 100
    @Override
    public double getGainOverTime(LocalDate startDate,
                                  LocalDate endDate, String ticker) throws
            IOException {
      if (throwIOException) {
        log.append("getGainOverTimeIOException\n");
        throw new IOException("gainIOExceptionMessage");
      }
      return 100;
    }

    // Dummy data 200
    @Override
    public double getMovingDayAverage(LocalDate endDate, int days, String ticker) throws
            IOException {
      if (throwIOException) {
        log.append("getMovingDayAverageIOException\n");
        throw new IOException("averageIOExceptionMessage");
      }
      return 200;
    }

    // Dummy data dates
    @Override
    public List<LocalDate> getCrossover(LocalDate endDate, int days, String ticker) throws
            IOException {
      if (throwIOException) {
        log.append("getCrossoverIOException\n");
        throw new IOException("crossoverIOExceptionMessage");
      }
      log.append("getCrossover").append(endDate).append(days).append(ticker).append("\n");
      return List.of(LocalDate.of(1, 1, 1), LocalDate.of(2, 2, 2));
    }

    @Override
    public void createNewPortfolio(String name) {
      log.append("createNewPortfolio").append(name).append("\n");
    }

    @Override
    public void deletePortfolio(String name) {
      log.append("deletePortfolio").append(name).append("\n");
    }

    @Override
    public void renamePortfolio(String oldName, String newName) {
      log.append("renamePortfolio").append(oldName).append(newName).append("\n");
    }

    // Dummy data
    @Override
    public Map<String, Integer> getPortfolioContents(String name) {
      return Map.of("AAPL", 5, "AMZN", 10, "NFLX", 15);
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
      log.append("addStockToPortfolio").append(name).append(ticker).append(shares).append(date)
              .append("\n");
    }

    @Override
    public void addStockToPortfolio(String name, String ticker, int shares) {
      log.append("addStockToPortfolio").append(name).append(ticker).append(shares).append("\n");
    }

    @Override
    public void sellStockFromPortfolio(String name, String ticker, int shares, LocalDate date)
            throws IOException, IllegalArgumentException {
      if (throwIOException) {
        log.append("sellStockFromPortfolioIOException\n");
      }
      log.append("sellStockFromPortfolio").append(name).append(ticker).append(shares).append(date)
              .append("\n");

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
    public List<String> getPortfolioSaves(String name) throws IllegalArgumentException,
            IOException {
      if (throwIOException) {
        log.append("getPortfolioSavesIOException\n");
        throw new IOException("getPortfolioSavesIOExceptionMessage");
      }
      log.append("getPortfolioSaves").append(name).append("\n");
      return List.of("AA", "BB", "CC");
    }

    @Override
    public void loadPortfolioSave(String fileSaveName) throws IOException,
            IllegalArgumentException {
      log.append("loadPortfolioSave").append(fileSaveName).append("\n");
    }

    @Override
    public void createNewPortfolioSave(String name) throws IOException, IllegalArgumentException {
      log.append("createNewPortfolioSave").append(name).append("\n");

    }

    @Override
    public void rebalancePortfolio(String name, LocalDate date,
                                   Map<String, Double> proportions) throws IOException,
            IllegalArgumentException {
      log.append("rebalancePortfolio").append(name).append(date).append(proportions).append("\n");
    }

    @Override
    public Map<LocalDate, Double> getPortfolioPerformance(String name, LocalDate startDate,
                                                          LocalDate endDate) throws
            IllegalArgumentException, IOException {
      if (throwIOException) {
        log.append("getPortfolioPerformanceIOException\n");
        throw new IOException("getPortfolioPerformanceIOExceptionMessage");
      }
      log.append("getPortfolioPerformance").append(name).append(startDate).append(endDate)
              .append("\n");
      return Map.of(LocalDate.of(2024, 1, 1), 10.0, LocalDate.of(2024, 1, 2),
              15.0, LocalDate.of(2024, 1, 3), 20.0, LocalDate.of(2024, 1, 4), 15.0,
              LocalDate.of(2024, 1, 5), 25.0, LocalDate.of(2024, 1, 6), 5.0);
    }

    // Dummy data
    @Override
    public List<String> getPortfolios() {
      return List.of("S&P500", "NASDAQ");
    }

    // Dummy data 400
    @Override
    public double getPortfolioValue(String name, LocalDate date) throws IOException {
      if (throwIOException) {
        log.append("getPortfolioValueIOException\n");
        throw new IOException("portfolioValueIOExceptionMessage");
      }
      return 400;
    }

    @Override
    public void removeStockFromPortfolio(String name, String ticker) {
      log.append("removeStockFromPortfolio").append(name).append(ticker).append("\n");
    }

    // List of valid stocks is hardcoded.
    @Override
    public boolean stockExists(String ticker) throws IOException {
      return List.of("AAPL", "NFLX", "AMZN", "TSLA").contains(ticker);
    }
  }

  protected final class MockView implements PortfolioStockView {
    private StringBuilder log;

    public MockView(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void printWelcomeScreen() {
      log.append("printWelcomeScreen\n");
    }

    @Override
    public void printMainMenu() {
      log.append("printMainMenu\n");
    }

    @Override
    public void printMessage(String message) {
      log.append("printMessage").append(message).append("\n");
    }

    @Override
    public void printViewPortfolios(List<String> portfolios) {
      log.append("printViewPortfolios");
      for (var port : portfolios) {
        log.append(port);
      }
      log.append("\n");
    }

    @Override
    public void printManagePortfolio(Map<String, Integer> stocks, String name) {
      log.append("printManagePortfolios").append(name);
      List<String> list = new ArrayList<>();
      for (var s : stocks.entrySet()) {
        list.add(s.getKey());
        list.add(Integer.toString(s.getValue()));
      }
      Collections.sort(list);
      Collections.reverse(list);
      list.forEach(it -> log.append(it));
      log.append("\n");
    }

    @Override
    public void printXDayCrossovers(String ticker, LocalDate date, int days,
                                    List<LocalDate> dates) {
      log.append("printXDayCrossovers").append(ticker).append(date).append(days);
      for (var d : dates) {
        log.append(d);
      }
      log.append("\n");
    }

    @Override
    public void printStockGain(String ticker, LocalDate startDate, LocalDate endDate, double gain) {
      log.append("printStockGain").append(ticker).append(startDate).append(endDate).append(gain)
              .append("\n");
    }

    @Override
    public void printStockAverage(String ticker, LocalDate endDate, int days, double average) {
      log.append("printStockAverage").append(ticker).append(endDate).append(days).append(average)
              .append("\n");
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
    public void printPortfolioPerformance(Map<LocalDate, Double> performance, LocalDate startDate,
                                          LocalDate endDate) {
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
      log.append("printOptionsPrompt\n");
      log.append("printMenu\n");
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
    StockView view = new MockView(viewLog);
    StockModel model = new MockModel(modelLog, throwException);
    StockController controller = new BasicStockController(view, model, in);


    controller.run();

    assertEquals(expectedViewLog.toString(), viewLog.toString());
    assertEquals(expectedModelLog.toString(), modelLog.toString());

    return expectedViewLog.toString().equals(viewLog.toString()) && expectedModelLog.toString()
            .equals(modelLog.toString());
  }

  String tickerPrompt = "printMessagePlease enter the ticker of the stock that you would like to "
          + "know about:";
  String invalidDate = "printMessageInvalid date: Please enter a valid date.";
  String invalidDateFormat = "Invalid date: Please enter a valid date.";
  String invalidInputInteger = "printMessageInvalid input: not an integer, please try again.";
  String invalidInputDateOrder = "printMessageInvalid input: The end date must be after the "
          + "start" + " date.";
  String startDatePrompt = "printMessagePlease enter the starting date (inclusive)!";
  String endDatePrompt = "printMessagePlease enter the ending date (inclusive)!";
  String tickerIncorrect = "printMessageThat stock does not exist! Please try again.";
  String stockNotInPortfolio = "You haven't buy any stock to this portfolio at";

  String optionPrompt = "Please type the number that corresponds with the choice you would like "
          + "to pick, or type 0 to return/exit";

  String yearPrompt = "printMessagePlease input the year: ";
  String monthPrompt = "printMessagePlease input the month: ";
  String dayPrompt = "printMessagePlease input the day: ";

  @Test
  public void controllerExitsCorrectly() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("0")));
  }

  @Test
  public void controllerHandlesInvalidMenuInputs() {
    String invalidInputMessage = "printMessageInvalid input. Please enter a valid choice or "
            + BasicMenuOptions.exitKeyword() + " to exit the application.";
    assertTrue(runTest(false, prints("printMainMenu"), inputs("10"),
            prints(invalidInputMessage,
                    "printMainMenu"), inputs("500000"), prints(invalidInputMessage,
                    "printMainMenu"),
            inputs("a"), prints(invalidInputMessage, "printMainMenu"), inputs("-1"),
            prints(invalidInputMessage, "printMainMenu"), inputs("1+1"),
            prints(invalidInputMessage, "printMainMenu"), inputs("-10000"),
            prints(invalidInputMessage, "printMainMenu"), inputs("a"),
            prints(invalidInputMessage,
                    "printMainMenu"), inputs("&(*&91j"), prints(invalidInputMessage,
                    "printMainMenu"), inputs("0")));

  }

  @Test
  public void controllerHandlesInvalidTicker() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("1"),

            prints(tickerPrompt), inputs("1093"),

            prints(tickerIncorrect), inputs("auiqhroiq"),

            prints(tickerIncorrect), inputs("APPLE"),

            prints(tickerIncorrect), inputs("GOOGLE"),

            prints(tickerIncorrect), inputs("AAPL"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2005"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints("printStockGainAAPL2005-04-202024-04-20100.0"),
            prints("printMainMenu"),
            inputs("0")));


  }

  @Test
  public void controllerHandlesCaseInsensitiveTicker() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("1"),

            prints(tickerPrompt), inputs("aapl"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2005"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),
            prints("printStockGainAAPL2005-04-202024-04-20100.0"),
            prints("printMainMenu"),
            inputs("1"),

            prints(tickerPrompt), inputs("aAPl"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2005"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),
            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),
            prints("printStockGainAAPL2005-04-202024-04-20100.0"),
            prints("printMainMenu"),
            inputs("1"),

            prints(tickerPrompt), inputs("AAPL"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2005"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),
            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),
            prints("printStockGainAAPL2005-04-202024-04-20100.0"),
            prints("printMainMenu"),
            inputs("0")));


  }

  @Test
  public void controllerHandlesIncorrectDate() {

    assertTrue(runTest(false, prints("printMainMenu"), inputs("1"),

            prints(tickerPrompt), inputs("AAPL"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("a&(*aLm.]10-"),

            prints(monthPrompt),
            inputs("a"),

            prints(dayPrompt),
            inputs("20"),

            prints(invalidInputInteger),

            prints(yearPrompt),
            inputs("2005"),

            prints(monthPrompt),
            inputs("a"),

            prints(dayPrompt),
            inputs("20"),

            prints(invalidInputInteger),
            prints(yearPrompt),
            inputs("2005"),

            prints(monthPrompt),
            inputs("13"),

            prints(dayPrompt),
            inputs("20"),

            prints(invalidDate),
            prints(yearPrompt),
            inputs("2005"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("34"),

            prints(invalidDate),
            prints(yearPrompt),
            inputs("2012"),

            prints(monthPrompt),
            inputs("1"),

            prints(dayPrompt),
            inputs("32"),

            prints(invalidDate),
            prints(yearPrompt),
            inputs("2011"),

            prints(monthPrompt),
            inputs("13"),

            prints(dayPrompt),
            inputs("1"),

            prints(invalidDate),

            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),

            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("19"),

            prints(invalidInputDateOrder), prints("printMainMenu"), inputs("1"),

            prints(tickerPrompt), inputs("AAPL"),

            prints(startDatePrompt),

            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),
            prints(endDatePrompt),

            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(invalidInputDateOrder), prints("printMainMenu"), inputs("1"),

            prints(tickerPrompt), inputs("AAPL"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),
            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("21"),
            prints("printStockGainAAPL2013-04-202013-04-21100.0"),
            prints("printMainMenu"),
            inputs("1"),

            prints(tickerPrompt), inputs("AAPL"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("30"),
            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("31"),

            prints("printStockGainAAPL2023-12-302023-12-31100.0"),
            prints("printMainMenu"),
            inputs("1"),

            prints(tickerPrompt), inputs("AAPL"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("1"),

            prints(dayPrompt),
            inputs("1"),
            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("31"),
            prints("printStockGainAAPL2023-01-012023-12-31100.0"),
            prints("printMainMenu"),
            inputs("1"),

            prints(tickerPrompt), inputs("AAPL"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("6"),

            prints(dayPrompt),
            inputs("5"),

            prints("printStockGainAAPL2024-04-202024-06-05100.0"),
            prints("printMainMenu"),
            inputs("0")));

  }


  @Test
  public void movingAverageWorks() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("2"),

            prints(tickerPrompt), inputs("NFLX"),

            prints("printMessagePlease enter the ending date!"),
            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints("printMessagePlease enter the number of days."), inputs("100"),

            prints("printStockAverageNFLX2013-04-20100200.0"), prints("printMainMenu"),
            inputs("0")));

  }

  @Test
  public void crossoversWorks() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("3"),

            prints(tickerPrompt), inputs("AAPL"),

            prints("printMessagePlease enter the ending date!"),
            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),
            prints("printMessagePlease enter the number of days."), inputs("13"), modelLog(
                    "getCrossover2013-04-2013AAPL"),

            prints("printXDayCrossoversAAPL"
                    + "2013-04-2013"
                    + "0001-01-01"
                    + "0002-02-02"),
            prints("printMainMenu"), inputs("0")));


  }

  @Test
  public void createOnePortfolioWorks() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),

            prints("printMessageWhat is the name of the portfolio you would like to create?"),
            inputs("ADOBE"), modelLog("createNewPortfolioADOBE"),

            prints("printMessageSuccessfully created portfolio ADOBE."), prints(
                    "printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));

  }

  @Test
  public void createDuplicatePortfoliosDoesntWork() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),

            prints("printMessageWhat is the name of the portfolio you would like to create?"),
            inputs("ADOBE"), modelLog("createNewPortfolioADOBE"),

            prints("printMessageSuccessfully created portfolio ADOBE."), prints(
                    "printViewPortfoliosS&P500NASDAQ"), inputs("1"),

            prints("printMessageWhat is the name of the portfolio you would like to create?"),
            inputs("S&P500"),

            prints("printMessageA portfolio with that name already exists!"), prints(
                    "printViewPortfoliosS&P500NASDAQ"), inputs("1"),

            prints("printMessageWhat is the name of the portfolio you would like to create?"),
            inputs("AAAAAA"), modelLog("createNewPortfolioAAAAAA"),

            prints("printMessageSuccessfully created portfolio AAAAAA."), prints(
                    "printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));

  }

  @Test
  public void deleteProfileWorks() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("2"),

            prints("printMessageWhat portfolio would you like to delete?"), inputs("NASDAQ"),
            modelLog("deletePortfolioNASDAQ"),

            prints("printMessageSuccessfully deleted portfolio NASDAQ."), prints(
                    "printViewPortfoliosS&P500NASDAQ"), inputs("2"),

            prints("printMessageWhat portfolio would you like to delete?"), inputs("S&P50"),

            prints("printMessageA portfolio with that name does not exist!"), prints(
                    "printViewPortfoliosS&P500NASDAQ"), inputs("2"),

            prints("printMessageWhat portfolio would you like to delete?"), inputs("AAPL"),

            prints("printMessageA portfolio with that name does not exist!"), prints(
                    "printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));


  }

  @Test
  public void renameProfileWorks() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("3"),

            prints("printMessageWhat portfolio would you like to rename? "
                    + "(Please enter the name)"
                    + "."), inputs("NASDAQ"),

            prints("printMessageWhat would you like to rename this portfolio to?"), inputs(
                    "AAAAAA"), modelLog("renamePortfolioNASDAQAAAAAA"),

            prints("printMessageSuccessfully renamed portfolio NASDAQ to AAAAAA."), prints(
                    "printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));


  }

  @Test
  public void controllerHandlesGainIOException() {
    assertTrue(runTest(true, prints("printMainMenu"), inputs("1"),

            prints(tickerPrompt), inputs("AAPL"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2020"),

            prints(monthPrompt),
            inputs("1"),

            prints(dayPrompt),
            inputs("1"),

            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2021"),

            prints(monthPrompt),
            inputs("1"),

            prints(dayPrompt),
            inputs("1"),

            modelLog("getGainOverTimeIOException"), prints("printMessageError "
                    + "while fetching "
                    + "data: gainIOExceptionMessage"), prints("printMainMenu"),
            inputs("0")));

  }

  @Test
  public void controllerHandlesCrossoverIOException() {
    assertTrue(runTest(true, prints("printMainMenu"), inputs("3"),

            prints(tickerPrompt), inputs("AAPL"),

            prints("printMessagePlease enter the ending date!"),
            prints(yearPrompt),
            inputs("2021"),

            prints(monthPrompt),
            inputs("1"),

            prints(dayPrompt),
            inputs("1"),

            prints("printMessagePlease enter the number of days."), inputs("50"),

            modelLog("getCrossoverIOException"), prints("printMessageError while "
                    + "fetching data: "
                    + "crossoverIOExceptionMessage"), prints("printMainMenu"),
            inputs("0")));

  }

  @Test
  public void controllerHandlesAverageIOException() {
    assertTrue(runTest(true, prints("printMainMenu"), inputs("2"),

            prints(tickerPrompt), inputs("AAPL"),

            prints("printMessagePlease enter the ending date!"),

            prints(yearPrompt),
            inputs("2021"),

            prints(monthPrompt),
            inputs("1"),

            prints(dayPrompt),
            inputs("1"),

            prints("printMessagePlease enter the number of days."), inputs("50"),

            modelLog("getMovingDayAverageIOException"), prints("printMessageError"
                    + " while fetching "
                    + "data: averageIOExceptionMessage"), prints("printMainMenu"),
            inputs("0")));

  }

  @Test
  public void renameNonExistentPortfolio() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("3"),

            prints("printMessageWhat portfolio would you like to rename? "
                    + "(Please enter the name)"
                    + "."), inputs("S&N500"),

            prints("printMessageA portfolio with that name does not exist!"), prints(
                    "printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));

  }

  @Test
  public void renamePortfolioToExistingName() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("3"),

            prints("printMessageWhat portfolio would you like to rename? "
                    + "(Please enter the name)"
                    + "."), inputs("S&P500"),

            prints("printMessageWhat would you like to rename this portfolio to?"), inputs(
                    "NASDAQ"),

            prints("printMessageA portfolio with that name already exists!"), prints(
                    "printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));

  }

  @Test
  public void viewPortfoliosMenuHandlesInvalidInput() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("-1"),

            prints("printMessageInvalid input. Please enter a valid choice or "
                    + "0 to go back."), prints("printViewPortfoliosS&P500NASDAQ"),
            inputs("10"),

            prints("printMessageInvalid input. Please enter a valid choice "
                    + "or 0 to go back."), prints("printViewPortfoliosS&P500NASDAQ"),
            inputs("a"),

            prints("printMessageInvalid input. Please enter a valid choice "
                    + "or 0 to go back."), prints("printViewPortfoliosS&P500NASDAQ"),
            inputs("-1"),

            prints("printMessageInvalid input. Please enter a valid choice "
                    + "or 0 to go back."), prints("printViewPortfoliosS&P500NASDAQ"),
            inputs("0"),

            prints("printMainMenu"), inputs("0")));

  }

  @Test
  public void addStockWorksForMultipleStocks() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),


            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),

            prints("printMessageWhat is the name of the portfolio you would"
                    + " like to create?"), inputs("ASjkdsf"),
            prints("printMessageSuccessfully created portfolio ASJKDSF."),
            modelLog("createNewPortfolioASJKDSF"),
            prints("printViewPortfoliosS&P500NASDAQ"),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("4"),
            inputs("1"),
            prints("printMessagePlease enter the ticker of the stock that you would "
                    + "like to add to portfolio S&P500:"),inputs("AAPL"),
            prints("printMessagePlease enter the number of shares you would like to "
                    + "purchase (you cannot buy fractional number of stocks): "),
            inputs("19"),
            prints("printMessagePlease enter the date in the format MM/DD/YYYY: "),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("23"),

            prints("printMessageSuccessfully purchased 19 shares of"
                    + " AAPL stocks at date 2023-12-23 in the S&P500 portfolio."),
            modelLog("addStockToPortfolioS&P500AAPL192023-12-23"),
            prints("printMessage"),
            prints("printOptionsPrompt"),
            prints("printMenu"), inputs("0"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"),
            inputs("0")));
  }

  @Test
  public void addStockToPortfolioWorksWithInvalidTickers() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),


            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),


            prints("printMessageWhat is the name of the portfolio you would like"
                    + " to create?"), inputs("ASjkdsf"),
            prints("printMessageSuccessfully created portfolio ASJKDSF."),
            modelLog("createNewPortfolioASJKDSF"),
            prints("printViewPortfoliosS&P500NASDAQ"),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("4"),
            inputs("1"),
            prints("printMessagePlease enter the ticker of the stock that you would like "
                    + "to add to portfolio S&P500:"),inputs("asdfs"),
            prints(tickerIncorrect), inputs("$%^YTG"),

            prints(tickerIncorrect), inputs("7654rfgy"),

            prints(tickerIncorrect), inputs("5rtgfd"),

            prints(tickerIncorrect), inputs("4ewsdfg"),

            prints(tickerIncorrect), inputs("WSDF"),

            prints(tickerIncorrect), inputs("AAPL"),
            prints("printMessagePlease enter the number of shares you would like to "
                    + "purchase (you cannot buy fractional number of stocks): "),
            inputs("19"),
            prints("printMessagePlease enter the date in the format MM/DD/YYYY: "),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("23"),
            prints("printMessageSuccessfully purchased 19 shares of AAPL stocks at date "
                    + "2023-12-23 in the S&P500 portfolio."),
            modelLog("addStockToPortfolioS&P500AAPL192023-12-23"),
            prints("printMessage"),
            prints("printOptionsPrompt"), prints("printMenu"),inputs("0"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));
  }

  @Test
  public void addStockHandlesInvalidShareInputs() {

    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),

            prints("printMessageWhat is the name of the portfolio you would "
                    + "like to create?"), inputs("ASjkdsf"),
            prints("printMessageSuccessfully created portfolio ASJKDSF."),
            modelLog("createNewPortfolioASJKDSF"),
            prints("printViewPortfoliosS&P500NASDAQ"),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("4"),

            inputs("1"),
            prints("printMessagePlease enter the ticker of the stock that you would like to "
                    + "add to portfolio S&P500:"),inputs("AAPL"),
            prints("printMessagePlease enter the number of shares you would like to " +
                    "purchase " + "(you" + " cannot buy fractional number of stocks): "),
            inputs("$%^&"),
            prints("printMessageInvalid input: not an integer, please try again."),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("1"),

            prints("printMessagePlease enter the ticker of the stock that you would like"
                    + " to add "
                    + "to portfolio S&P500:"), inputs("AAPL"),

            prints("printMessagePlease enter the number of shares you would like to "
                    + "purchase "
                    + "(you" + " cannot buy fractional number of stocks): "), inputs("10.5"),
            prints("printMessageInvalid input: not an integer, please try again."),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("1"),

            prints("printMessagePlease enter the ticker of the stock that you would like "
                    + "to add "
                    + "to portfolio S&P500:"), inputs("AAPL"),

            prints("printMessagePlease enter the number of shares you would like to purchase "
                    + "(you" + " cannot buy fractional number of stocks): "), inputs("-10"),
            prints("printMessageCannot purchase negative number of stocks."),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("1"),

            prints("printMessagePlease enter the ticker of the stock that you would like "
                    + "to add "
                    + "to portfolio S&P500:"), inputs("AAPL"),

            prints("printMessagePlease enter the number of shares you would like to purchase "
                    + "(you" + " cannot buy fractional number of stocks): "), inputs("0"), prints(
                    "printMessageCannot purchase 0 shares of a stock."),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("1"),

            prints("printMessagePlease enter the ticker of the stock that you would like "
                    + "to add "
                    + "to portfolio S&P500:"), inputs("AAPL"),

            prints("printMessagePlease enter the number of shares you would like to purchase "
                    + "(you" + " cannot buy fractional number of stocks): "), inputs("50"),
            prints("printMessagePlease enter the date in the format MM/DD/YYYY: "),

            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("23"),

            prints("printMessageSuccessfully purchased 50 shares of AAPL stocks at date "
                    + "2023-12-23 in the S&P500 portfolio."),
            modelLog("addStockToPortfolioS&P500AAPL502023-12-23"),
            prints("printMessage"),
            prints("printOptionsPrompt"),
            prints("printMenu"), inputs("2"),

            prints("printMessagePlease enter the date that you want to sell the stocks!"),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("30"),
            prints("printMessagePlease enter the ticker of the "
                    + "stock that you would like to sell from portfolio S&P500:")


            ,inputs("AAPL"),
            prints("printMessagePlease enter the number of shares you would like to sell "
                    + "(you cannot sell fractional number of stocks): "),
            inputs("12"), prints("printMessageSuccessfully sold 12 number of AAPL stocks "
                    + "from date 2023-12-30 in the S&P500 portfolio."),
            modelLog("sellStockFromPortfolioS&P500AAPL122023-12-30"),
            prints("printOptionsPrompt"), prints("printMenu"),inputs("0"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));
  }

  @Test
  public void removeStockFromPortfolio() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),


            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),


            prints("printMessageWhat is the name of the portfolio you would like to create?"),
            inputs("ASjkdsf"),
            prints("printMessageSuccessfully created portfolio ASJKDSF."),
            modelLog("createNewPortfolioASJKDSF"),
            prints("printViewPortfoliosS&P500NASDAQ"),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("4"),

            inputs("1"),
            prints("printMessagePlease enter the ticker of the stock that you would like to "
                    + "add to portfolio S&P500:"),inputs("AAPL"),
            prints("printMessagePlease enter the number of shares you would like to purchase "
                    + "(you cannot buy fractional number of stocks): "),
            inputs("19"),
            prints("printMessagePlease enter the date in the format MM/DD/YYYY: "),

            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("23"),
            prints("printMessageSuccessfully purchased 19 shares of AAPL stocks at date 2"
                    + "023-12-23 in the S&P500 portfolio."),
            modelLog("addStockToPortfolioS&P500AAPL192023-12-23"),
            prints("printMessage"),
            prints("printOptionsPrompt"),
            prints("printMenu"), inputs("2"),

            prints("printMessagePlease enter the date that you want to sell the stocks!"),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("30"),
            prints("printMessagePlease enter the ticker of the stock "
                    + "that you would like to sell from portfolio S&P500:")


            ,inputs("AAPL"),
            prints("printMessagePlease enter the number of shares you would like to sell "
                    + "(you cannot sell fractional number of stocks): "),
            inputs("12"), prints("printMessageSuccessfully sold 12 number of AAPL stocks "
                    + "from date 2023-12-30 in the S&P500 portfolio."),
            modelLog("sellStockFromPortfolioS&P500AAPL122023-12-30"),
            prints("printOptionsPrompt"), prints("printMenu"),inputs("0"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));

  }

  @Test
  public void removeStockHandlesInvalidInputs() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),


            prints("printMessageWhat is the name of the portfolio you would like to"
                    + " create?"), inputs("ASjkdsf"),
            prints("printMessageSuccessfully created portfolio ASJKDSF."),
            modelLog("createNewPortfolioASJKDSF"),
            prints("printViewPortfoliosS&P500NASDAQ"),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("4"),
            inputs("1"),
            prints("printMessagePlease enter the ticker of the stock that you would like "
                    + "to add to portfolio S&P500:"),inputs("AAPL"),
            prints("printMessagePlease enter the number of shares you would like to purchase "
                    + "(you cannot buy fractional number of stocks): "),
            inputs("19"),
            prints("printMessagePlease enter the date in the format MM/DD/YYYY: "),

            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("23"),
            prints("printMessageSuccessfully purchased 19 shares of AAPL stocks at "
                    + "date 2023-12-23 in the S&P500 portfolio."),
            modelLog("addStockToPortfolioS&P500AAPL192023-12-23"),
            prints("printMessage"),
            prints("printOptionsPrompt"),
            prints("printMenu"), inputs("2"),

            prints("printMessagePlease enter the date that you want to sell the stocks!"),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("30"),
            prints("printMessagePlease enter the ticker of the stock "
                    + "that you would like to sell from portfolio S&P500:"),


            inputs("XYZ123"), prints("printMessagePlease enter the stock that you bought "
                    + "before that date: "), inputs("GOOGL"),  prints("printMessagePlease "
                    + "enter the stock that you bought before that date: "), inputs("NFLX100"),
            prints("printMessagePlease enter the stock that you bought before that date: "),
            inputs("AAPL"),
            prints("printMessagePlease enter the number of shares you would like to sell "
                    + "(you cannot sell fractional number of stocks): "),
            inputs("12"), prints("printMessageSuccessfully sold 12 number of AAPL stocks"
                    + " from date 2023-12-30 in the S&P500 portfolio."),
            modelLog("sellStockFromPortfolioS&P500AAPL122023-12-30"),
            prints("printOptionsPrompt"), prints("printMenu"),inputs("0"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));

  }

  @Test
  public void managePortfolioMenuHandlesInvalidInput() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),
            prints("printViewPortfoliosS&P500NASDAQ"), inputs("invalid"),

            prints("printMessageInvalid input. Please enter a valid choice or 0 "
                    + "to go back."), prints("printViewPortfoliosS&P500NASDAQ"), inputs("10"),

            prints("printMessageInvalid input. Please enter a valid choice or 0"
                    + " to go back."), prints("printViewPortfoliosS&P500NASDAQ"), inputs("-1"),

            prints("printMessageInvalid input. Please enter a valid choice or 0"
                    + " to go back."), prints("printViewPortfoliosS&P500NASDAQ"),
            inputs("1+1"),

            prints("printMessageInvalid input. Please enter a valid choice or 0 "
                    + "to go back."), prints("printViewPortfoliosS&P500NASDAQ"),
            inputs("-5"),

            prints("printMessageInvalid input. Please enter a valid choice or 0 "
                    + "to go back."), prints("printViewPortfoliosS&P500NASDAQ"),
            inputs("2.5"),

            prints("printMessageInvalid input. Please enter a valid choice or 0 "
                    + "to go back."), inputs("0"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));

  }

  @Test
  public void controllerHandlesGetPortfolioValueIOException() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),

            prints("printMessageWhat is the name of the portfolio you would like to create?"),
            inputs("01/01/2021"),
            modelLog("createNewPortfolio01/01/2021"),

            prints("printMessageSuccessfully created portfolio 01/01/2021."),


            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));

  }

  @Test
  public void calculatePortfolioValue() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),

            prints("printMessageWhat is the name of the portfolio you would like to create?"),
            inputs("PortfoliosS&P500NASDAQ"),
            modelLog("createNewPortfolioPORTFOLIOSS&P500NASDAQ"),


            prints("printMessageSuccessfully created portfolio PORTFOLIOSS&P500NASDAQ."),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));

  }

  @Test
  public void xDayCrossoverHandlesInvalidDaysInput() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("3"),

            prints(tickerPrompt), inputs("AAPL"),

            prints("printMessagePlease enter the ending date!"),
            prints(yearPrompt),
            inputs("2021"),

            prints(monthPrompt),
            inputs("1"),

            prints(dayPrompt),
            inputs("1"),

            prints("printMessagePlease enter the number of days."), inputs("abcd"), prints(
                    "printMessageInvalid input: not an integer, please try again."),
            inputs("$"
                    + "%^&"),
            prints("printMessageInvalid input: not an integer, please try again."),
            inputs("10.5"),
            prints("printMessageInvalid input: not an integer, please try again."),
            inputs("-10"),
            prints("printMessageInvalid input: not a valid number. Please enter a "
                    + "number from 1"
                    + " " + "to 2147483647"), inputs("100"), modelLog("getCrossover2021-01"
                    + "-01100AAPL"), prints("printXDayCrossoversAAPL2021-01-011000001-01"
                    + "-010002-02"
                    + "-02"),

            prints("printMainMenu"), inputs("0")));
  }

  @Test
  public void DisplayPortfolio() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),
            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),


            prints("printMessageWhat is the name of the portfolio you would like to create?"),
            inputs("ASjkdsf"),
            prints("printMessageSuccessfully created portfolio ASJKDSF."),
            modelLog("createNewPortfolioASJKDSF"),
            prints("printViewPortfoliosS&P500NASDAQ"),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("4"),

            inputs("9"),
            prints("printMessageWhat date would you like to know the composition of portfolio"
                    + " S&P500 at?"),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("23"),
            prints("printManagePortfolioDoubleS&P500NFLXAMZNAAPL5.515.510.5"),

            prints("printOptionsPrompt"),
            prints("printMenu"), inputs("0"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));
  }

  @Test
  public void DistributionWithDate() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),
            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),


            prints("printMessageWhat is the name of the portfolio you would like to create?"),
            inputs("ASjkdsf"),
            prints("printMessageSuccessfully created portfolio ASJKDSF."),
            modelLog("createNewPortfolioASJKDSF"),
            prints("printViewPortfoliosS&P500NASDAQ"),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("4"),

            inputs("4"),
            prints("printMessageWhat date would you like to know the value of portfolio "
                    + "S&P500 at?"),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("23"),            prints("printDistributionS&P500GOOGAMZNAAPL0.250.250.25"),
            modelLog("getPortfolioDistributionS&P5002023-12-23"),
            prints("printOptionsPrompt"),
            prints("printMenu"), inputs("0"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));
  }

  @Test
  public void LoadPortfolio() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),
            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),


            prints("printMessageWhat is the name of the portfolio you would like to create?"),
            inputs("ASjkdsf"),
            prints("printMessageSuccessfully created portfolio ASJKDSF."),
            modelLog("createNewPortfolioASJKDSF"),
            prints("printViewPortfoliosS&P500NASDAQ"),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("4"),

            inputs("5"),

            prints("printMessageYou have successfully saved the portfolio!"),
            modelLog("createNewPortfolioSaveS&P500"),
            prints("printMessage"), prints("printOptionsPrompt"),
            prints("printMenu"), inputs("0"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));
  }

  @Test
  public void PerformanceOverTime() {
    assertTrue(runTest(false, prints("printMainMenu"), inputs("4"),
            prints("printViewPortfoliosS&P500NASDAQ"), inputs("1"),


            prints("printMessageWhat is the name of the portfolio you would like to create?"),
            inputs("ASjkdsf"),
            prints("printMessageSuccessfully created portfolio ASJKDSF."),
            modelLog("createNewPortfolioASJKDSF"),
            prints("printViewPortfoliosS&P500NASDAQ"),
            prints("printOptionsPrompt"),
            prints("printMenu"),
            inputs("4"),

            inputs("8"),

            prints("printMessagePlease enter the starting date (inclusive)!"),
            prints(yearPrompt),
            inputs("2023"),

            prints(monthPrompt),
            inputs("12"),

            prints(dayPrompt),
            inputs("23"),
            prints("printMessagePlease enter the ending date (inclusive)!"),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("5"),

            prints(dayPrompt),
            inputs("12"),
            prints("printMessagePerformance of portfolio S&P500 from 2023-12-23 to "
                    + "2024-05-12"),
            prints("printPortfolioPerformance2023-12-232024-05-125.025.02024-01-062024-01-"
                    + "052024-01-042024-01-032024-01-022024-01-0120.015.015.010.0"),
            modelLog("getPortfolioPerformanceS&P5002023-12-232024-05-12"),
            modelLog("getPortfolioPerformanceS&P5002023-12-232024-05-12"),
            prints("printMessage"), prints("printOptionsPrompt"),
            prints("printMenu"), inputs("0"),

            prints("printViewPortfoliosS&P500NASDAQ"), inputs("0"),

            prints("printMainMenu"), inputs("0")));
  }
}