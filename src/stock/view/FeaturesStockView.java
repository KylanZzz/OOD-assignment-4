package stock.view;

import java.util.List;
import java.util.Map;

import stock.controller.PortfolioStockFeatures;

/**
 * An interface that contains allows the view to associates with the GUI. The view is able to
 * display the composition, display value, display bought stock, display sold stock, and other
 * abilities towards to the portfolio itself.
 */
public interface FeaturesStockView {

  /**
   * To addActionListener for the view.
   *
   * @param features the controller of the GUI.
   */
  void addFeatures(PortfolioStockFeatures features);


  /**
   * Display the composition of the certain portfolio.
   *
   * @param composition the ticker, and the shares in the portfolio.
   */
  void displayComposition(Map<String, Double> composition);

  /**
   * Display the value of the portfolio.
   *
   * @param value the value of the portfolio.
   */
  void displayValue(double value);

  /**
   * Display the amount of shares and the ticker that the user bought.
   *
   * @param ticker the name of the stock.
   * @param shares the quantity of the stock.
   */
  void displayBoughtStock(String ticker, double shares);

  /**
   * Display the amount of shares and the ticker that the user sold.
   *
   * @param ticker the name of the stock.
   * @param shares the quantity of the stock.
   */
  void displaySoldStock(String ticker, double shares);

  /**
   * Display the portfolio list.
   *
   * @param names the name of the portfolios.
   */
  void displayPortfolios(List<String> names);

  /**
   * Display the portfolio been saved.
   *
   * @param name the name of the portfolio.
   */
  void displayCreatedSave(String name);

  /**
   * Display the portfolio that been created.
   *
   * @param portfolio the name of the portfolio.
   */
  void displayCreatedPortfolio(String portfolio);

  /**
   * To display the loaded portfolio.
   *
   * @param portfolio the name of the portfolio.
   */
  void displayLoadedPortfolio(String portfolio);

  /**
   * To create a window to manage the portfolio.
   *
   * @param portfolio the name of the portfolio.
   */
  void displayEditPortfolio(String portfolio);

  /**
   * Display the error message.
   *
   * @param message the error message.
   */
  void displayErrorMessage(String message);
}
