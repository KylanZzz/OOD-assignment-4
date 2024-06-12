package stock.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Defines the contract for a user interface component in a stock market application.
 * Implementations of this interface are responsible for;
 * displaying various types of stock-related information to the user.
 */
public interface StockView {

  /**
   * Displays a welcome screen to the user.
   */
  void printWelcomeScreen();

  /**
   * Provides the main menu to the user.
   */
  void printMainMenu();


  /**
   * Display the message based on the actions that the user did.
   *
   * @param message the message to direct the user.
   */
  void printMessage(String message);

  /**
   * Display a list of portfolios.
   *
   * @param portfolios a list of portfolio that the user creates.
   */
  void printViewPortfolios(List<String> portfolios);

  /**
   * Display the list of commands that the user can ask for.
   *
   * @param stocks the ticker of the stocks and the quantity of the stocks.
   * @param name the name of the portfolio.
   */
  void printManagePortfolio(Map<String, Integer> stocks, String name);

  /**
   * Display the x day of the crossovers.
   *
   * @param ticker the name of the stock.
   * @param date the endDate of the stock.
   * @param days the x day.
   * @param dates the list of the dates that are crossovers.
   */
  void printXDayCrossovers(String ticker, LocalDate date, int days, List<LocalDate> dates);

  /**
   * Display the amount of gain and loss of the stock.
   *
   * @param ticker the name of the stock.
   * @param startDate the startDate of the stock.
   * @param endDate the endDate of teh stock.
   * @param gain the amount of gain and loss.
   */
  void printStockGain(String ticker, LocalDate startDate, LocalDate endDate, double gain);

  /**
   * Display the moving average of a stock of a period.
   *
   * @param ticker the name of the stock.
   * @param endDate the end date of the stock.
   * @param days the x day.
   * @param average the moving average.
   */
  void printStockAverage(String ticker, LocalDate endDate, int days, double average);


}
