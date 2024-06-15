package stock.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Defines the contract for a user interface component in a stock market application.
 * Implementations of this interface are responsible for;
 * displaying various types of stock-related information to the user.
 */
public interface PortfolioStockView extends StockView {
  /**
   * Display the list of commands that the user can ask for.
   *
   * @param stocks the ticker of the stocks and the fractional quantity of the stocks.
   * @param name the name of the portfolio.
   */
  void printManagePortfolioDouble(Map<String, Double> stocks, String name);

  /**
   * Display the list of commands that the user can ask for.
   *
   * @param stocks the ticker of the stocks and the fractional quantity of the stocks.
   * @param name the name of the portfolio.
   */
  void printDistribution(Map<String, Double> stocks, String name, LocalDate date);

  /**
   * Display the list of commands that the user can ask for.
   *
   * @param fileName the name of the saved files.
   */
  void printFileSaveName(List<String> fileName);

  /**
   * Display the chart of the performance of portfolio that the user can ask for.
   *
   * @param performance the performance of the portfolio.
   * @param startDate the start date to calculate the performance of the portfolio.
   * @param endDate the end date to calculate the performance of the portfolio.
   */
  void printPortfolioPerformance(Map<LocalDate, Double> performance,
                                  LocalDate startDate,
                                  LocalDate endDate);

  /**
   * Display the option of the portfolio to the user.
   */
  void printPortfolioOption();

}
