package stock.model;

import java.time.LocalDate;
import java.util.List;

public interface StockModel {
//  List<Portfolio> portfolios;
//
//  class Portfolio {
//    String name;
//    List<Stock> stocks;
//
//  }
//
//  class Stock {
//    String ticker;
//  }

  /**
   * 1. Get the gain/loss of stock over period of time
   * 2. Get x-day moving average of a stock
   * 3. Get x-day crossovers for a stock
   * 4. Manage portfolios
   *
   * Type 4...
   *
   * 1. Create new portfolio
   * 2. Delete portfolio
   * 3. Rename portfolio
   * 4. Portfolio 1...
   * 5. Portfolio 2...
   * 6. Portfolio 3...
   *
   * Type 4...
   *
   * // Display portfolio1 contents
   *
   * 1. Calculate portfolio value
   * 2. Add stock to portfolio
   * 3. Remove stock from portfolio
   */


  /**
   * Get the gain/loss of a stock.
   *
   * @param startDate the start date for calculating the gain/loss.
   * @param endDate the end date for calculating the cost.
   * @param ticker the ticker of the Stock.
   * @return the amount of the cost that been gain/lost in x-day (positive for a gain and
   *         negative for a loss)
   */
  double getGainOverTime(LocalDate startDate, LocalDate endDate, String ticker);

  /**
   * Get the x-day moving average for a stock.
   *
   * @param endDate the end date for calculating the moving average. This is the date up to which
   *                the average is calculated, and the calculation will count the last x days
   *                before this date (including the end date itself).
   * @param days the number of days to calculate the average.
   * @param ticker the ticker of the Stock.
   * @return the x-day moving average.
   */
  double getMovingDayAverage(LocalDate endDate, int days, String ticker);

  /**
   *
   * @param startDate the start day of the crossover.
   * @param endDate the end day of the crossover.
   * @param days the amount of period to examine the crossover.
   * @param ticker the ticker of the stock.
   * @return which days are x-day crossovers
   */
  double getCrossover(LocalDate startDate, LocalDate endDate, int days, String ticker);

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
   * Get the stocks in a portfolio.
   *
   * @param name the name of the portfolio.
   * @return the list of options within the contents.
   */
  List<String> getPortfolioContents(String name);

  /**
   * A list of portfolios created by the user.
   *
   * @return a list of portfolios
   */
  List<String> getPortfolios();

  /**
   * Get the value of a portfolio on a specific date.
   *
   * @param name the name of the portfolio.
   * @param date the date to get value at.
   * @return the value of the portfolio.
   */
  double getPortfolioValue(String name, LocalDate date);

  /**
   * Add a new stock to a certain portfolio.
   * @param name the name of the portfolio.
   * @param ticker the ticker of the stock.
   */
  void addStockToPortfolio(String name, String ticker);

  /**
   * Remove a stock from a certain portfolio.
   * @param name the name of the portfolio.
   * @param ticker the ticker of the stock.
   */
  void removeStockFromPortfolio(String name, String ticker);
}
