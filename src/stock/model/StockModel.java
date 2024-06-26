package stock.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.List;

/**
 * The StockModel interface provides methods for managing stock portfolios and
 * performing various financial calculations such as calculating gains, moving averages,
 * and detecting crossovers.
 * It also includes methods for portfolio management, such as creating,
 * deleting, and renaming portfolios, as well as adding and removing stocks from portfolios.
 */
public interface StockModel {

  /**
   * Get the gain/loss of a stock.
   *
   * @param startDate the start date for calculating the gain/loss.
   * @param endDate   the end date for calculating the cost.
   * @param ticker    the ticker of the Stock.
   * @return          the amount of the cost that been gain/lost in x-day (positive for a gain and
   *                  negative for a loss)
   * @throws IOException if an I/O error occurs during data fetching.
   */
  double getGainOverTime(LocalDate startDate, LocalDate endDate, String ticker) throws IOException;

  /**
   * Get the x-day moving average for a stock.
   *
   * @param endDate the end date for calculating the moving average. This is the date up to which
   *                the average is calculated, and the calculation will count the last x days
   *                before this date (including the end date itself).
   * @param days    the number of days to calculate the average.
   * @param ticker  the ticker of the Stock.
   * @return the x-day moving average.
   * @throws IOException if an I/O error occurs during data fetching.
   */
  double getMovingDayAverage(LocalDate endDate, int days, String ticker) throws IOException;

  /**
   * Get the crossover of x-day of the stock.
   *
   * @param endDate the end day of the crossover.
   * @param days    the amount of period to examine the crossover.
   * @param ticker  the ticker of the stock.
   * @return which days are x-day crossovers
   * @throws IOException if an I/O error occurs during data fetching.
   */
  List<LocalDate> getCrossover(LocalDate endDate, int days, String ticker) throws IOException;

  /**
   * Create a new stock portfolio.
   *
   * @param name the name of the stock portfolio.
   */
  void createNewPortfolio(String name);

  /**
   * Create an existing stock portfolio.
   *
   * @param name the name of the stock portfolio.
   */
  void deletePortfolio(String name);

  /**
   * Renames a stock portfolio.
   *
   * @param oldName the old name of the portfolio.
   * @param newName the new name of the portfolio.
   */
  void renamePortfolio(String oldName, String newName);

  /**
   * Get the stocks and the corresponding shares in a portfolio.
   *
   * @param name the name of the portfolio.
   * @return      a map where the keys are the tickers of all the stocks and values are the number
   *              of shares of that stock in the portfolio.
   */
  Map<String, Integer> getPortfolioContents(String name);

  /**
   * A list of portfolios created by the user.
   *
   * @return a list of all portfolios names.
   */
  List<String> getPortfolios();

  /**
   * Get the value of a portfolio on a specific date.
   *
   * @param name the name of the portfolio.
   * @param date the date to get value at.
   * @return the value of the portfolio.
   * @throws IOException if an I/O error occurs during data fetching.
   */
  double getPortfolioValue(String name, LocalDate date) throws IOException;

  /**
   * Add a new stock to a certain portfolio. If the stock is already in the portfolio, then add the
   * specified number of shares of that stock to the portfolio.
   *
   * @param name   the name of the portfolio.
   * @param shares the number of shares of this stock.
   * @param ticker the ticker of the stock.
   */
  void addStockToPortfolio(String name, String ticker, int shares);

  /**
   * Remove a stock from a certain portfolio.
   *
   * @param name   the name of the portfolio.
   * @param ticker the ticker of the stock.
   */
  void removeStockFromPortfolio(String name, String ticker);

  /**
   * Check if a stock exists.
   *
   * @param ticker the ticker of the stock.
   * @return whether the stock exists.
   */
  boolean stockExists(String ticker) throws IOException;
}
