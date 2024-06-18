package stock.controller;

/**
 * An interface that helps to make a view for the stock by GUI
 */
public interface PortfolioStockFeatures {
  /**
   * To create a portfolio that the user creates.
   *
   * @param name the name of the portfolio.
   */
  void createPortfolio(String name);

  /**
   * To load the selected portfolio to the user.
   *
   * @param filePath the file path of the portfolio.
   */
  void loadPortfolio(String filePath);

  /**
   * To choose certain portfolio that the user wants.
   *
   * @param name the name of the chosen portfolio.
   */
  void choosePortfolio(String name);

  /**
   * To buy the stock to the portfolio with certain date.
   *
   * @param portfolio the name of the portfolio.
   * @param ticker the ticker of the stock.
   * @param shares the quantity of the stock.
   * @param month the month to buy the stock.
   * @param day the day to buy the stock.
   * @param year the year to buy the stock.
   */
  void buyStock(String portfolio, String ticker, String shares, String month, String day, String year);

  /**
   * To sell the stock from the portfolio with certain date.
   *
   * @param portfolio the name of the portfolio.
   * @param ticker the ticker of the stock.
   * @param shares the quantity of the stock.
   * @param month the month to sell the stock.
   * @param day the day to sell the stock.
   * @param year the year to sell the stock.
   */
  void sellStock(String portfolio, String ticker, String shares, String month, String day, String year);

  /**
   * To get the composition of the portfolio.
   *
   * @param portfolio the name of the portfolio.
   * @param month the month of the stock.
   * @param day the day of the stock.
   * @param year the year of the stock.
   * @param share the quantity of the stock.
   * @param ticker the name of the stock.
   */
  void getComposition(String portfolio, String month, String day, String year, String share, String ticker);

  /**
   * To get the total value of the portfolio.
   *
   * @param portfolio the name of the portfolio.
   * @param month the month of the stock.
   * @param day the day of the stock.
   * @param year the year of the stock
   * @param share the quantity of the stock.
   * @param ticker the name of the stock.
   */
  void getValue(String portfolio, String month, String day, String year, String share, String ticker);

  /**
   * To save the portfolio that created.
   *
   * @param portfolio the name of the portfolio.
   */
  void savePortfolio(String portfolio);
}
