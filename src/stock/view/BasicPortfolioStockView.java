package stock.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class BasicPortfolioStockView extends AbstractBasicStockView implements PortfolioStockView {
  private List<LocalDate> dateList;
  private static final DateTimeFormatter MONTH_YEAR_FORMATTER  = DateTimeFormatter.ofPattern("MMM yyyy");
  private static final DateTimeFormatter FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
  private static final int MAX_ASTERISKS = 50;
  private static final int MIN_LINES = 5;
  private static final int MAX_LINES = 30;
  private BasicStockView stockViewHelper;
  public BasicPortfolioStockView(Appendable out) {
    super(out);
    this.stockViewHelper = new BasicStockView(out);
    this.dateList = new ArrayList<>();
  }

  /**
   * Display the list of commands that the user can ask for.
   *
   * @param stocks the ticker of the stocks and the fractional quantity of the stocks.
   * @param name   the name of the portfolio.
   */
  @Override
  public void printManagePortfolioDouble(Map<String, Double> stocks, String name) {
    println(String.format("Here are all the stocks in the %s portfolio:\n", name));
    var list = stocks.keySet().stream().sorted()
            .map(it -> String.format("%-30s %-10.2f", it, stocks.get(it)))
            .collect(Collectors.toList());
    list.add(0, String.format("%-30s %s", "Stock", "Shares"));
    printList(list);
    println("");
  }



  /**
   * Displays a welcome screen to the user.
   */
  @Override
  public void printWelcomeScreen() {
    println("Welcome to OOD Assignment 4 for Stocks part 2!\n");
  }

  /**
   * Provides the main menu to the user.
   */
  @Override
  public void printMainMenu() {
    stockViewHelper.printMainMenu();
  }

  /**
   * Display the message based on the actions that the user did.
   *
   * @param message the message to direct the user.
   */
  @Override
  public void printMessage(String message) {
    stockViewHelper.printMessage(message);
  }

  /**
   * Display a list of portfolios.
   *
   * @param portfolios a list of portfolio that the user creates.
   */
  @Override
  public void printViewPortfolios(List<String> portfolios) {
    stockViewHelper.printViewPortfolios(portfolios);
  }

  /**
   * Display the list of commands that the user can ask for.
   *
   * @param stocks the ticker of the stocks and the quantity of the stocks.
   * @param name   the name of the portfolio.
   */
  @Override
  public void printManagePortfolio(Map<String, Integer> stocks, String name) {
    stockViewHelper.printManagePortfolio(stocks, name);
  }

  /**
   * Display the x day of the crossovers.
   *
   * @param ticker the name of the stock.
   * @param date   the endDate of the stock.
   * @param days   the x day.
   * @param dates  the list of the dates that are crossovers.
   */
  @Override
  public void printXDayCrossovers(String ticker, LocalDate date, int days, List<LocalDate> dates) {
    stockViewHelper.printXDayCrossovers(ticker, date, days, dates);
  }

  /**
   * Display the amount of gain and loss of the stock.
   *
   * @param ticker    the name of the stock.
   * @param startDate the startDate of the stock.
   * @param endDate   the endDate of teh stock.
   * @param gain      the amount of gain and loss.
   */
  @Override
  public void printStockGain(String ticker, LocalDate startDate, LocalDate endDate, double gain) {
    stockViewHelper.printStockGain(ticker, startDate, endDate, gain);
  }

  /**
   * Display the moving average of a stock of a period.
   *
   * @param ticker  the name of the stock.
   * @param endDate the end date of the stock.
   * @param days    the x day.
   * @param average the moving average.
   */
  @Override
  public void printStockAverage(String ticker, LocalDate endDate, int days, double average) {
    stockViewHelper.printStockAverage(ticker, endDate, days, average);
  }

  /**
   * Display the list of commands that the user can ask for.
   *
   * @param stocks the ticker of the stocks and the fractional quantity of the stocks.
   * @param name   the name of the portfolio.
   * @param date
   */
  @Override
  public void printDistribution(Map<String, Double> stocks, String name, LocalDate date) {
    println(String.format("Here are the distribution of the stocks in the %s portfolio at %s:\n", name, date));
    var list = stocks.keySet().stream().sorted()
            .map(it -> String.format("%-30s %-30s", it, '$' + String.format("%.2f", stocks.get(it))))
            .collect(Collectors.toList());
    list.add(0, String.format("%-30s %s", "Stock", "Values"));
    printList(list);
    println("");

  }

  /**
   * Display the list of commands that the user can ask for.
   *
   * @param fileName the name of the saved files.
   */
  @Override
  public void printFileSaveName(List<String> fileName) {
    for (int i = 0; i < fileName.size(); i++) {
      println(String.format("%d. %s", i + 1, fileName.get(i)));
    }
    println("");
    printOptionsPrompt();
  }

  /**
   * Display the chart of the performance of portfolio that the user can ask for.
   *
   * @param performance the performance of the portfolio.
   * @param startDate the start date to calculate the performance of the portfolio.
   * @param endDate the end date to calculate the performance of the portfolio.
   */
  @Override
  public void printPortfolioPerformance(Map<LocalDate, Double> performance, LocalDate startDate, LocalDate endDate) {
    if (performance == null || performance.isEmpty()) {
      println("No performance data available.");
      return;
    }

    long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

    DateTimeFormatter dateFormatter = daysBetween <= 31 ? FULL_DATE_FORMATTER : MONTH_YEAR_FORMATTER;

    Map<LocalDate, Double> adjustedPerformance = adjustDataPoints(performance, startDate, endDate, daysBetween);

    OptionalDouble maxOptional = adjustedPerformance.values().stream().mapToDouble(Double::doubleValue).max();
    if (!maxOptional.isPresent()) return;

    double max = maxOptional.getAsDouble();
    double scale = max > 0 ? MAX_ASTERISKS / max : 1;

    adjustedPerformance.forEach((date, value) -> {
      int numberOfAsterisks = (int) (value * scale);
      println(dateFormatter.format(date) + ": " + "*".repeat(numberOfAsterisks));
    });

    println("Scale: * = " + Math.round(max / MAX_ASTERISKS));
    println("");

  }

  @Override
  public void printPortfolioOption() {
    printOptionsPrompt();
    printMenu(BasicPortfolioMenuOptions.managePortfolio());
  }

  private Map<LocalDate, Double> adjustDataPoints(Map<LocalDate, Double> performance, LocalDate startDate, LocalDate endDate, long daysBetween) {
    if (daysBetween < MIN_LINES) {
      return performance;
    }

    int interval = (int) Math.ceil(daysBetween / (double) MAX_LINES);
    return performance.entrySet().stream()
            .filter(entry -> ChronoUnit.DAYS.between(startDate, entry.getKey()) % interval == 0)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

}
