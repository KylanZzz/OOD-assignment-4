package stock.controller;

import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;

/**
 * A simple implementation of a stock controller that extends on BasicStocker. It allows the user to analyze
 * stocks such as finding the x-day average, gain/loss over a period of time,
 * as well as creating portfolios and analyzing their value through text-based
 * input.
 */
public class PortfolioStockController extends BasicStockController {
  /**
   * Constructs a BasicStockController with the given view, model, and input
   * source.
   *
   * @param view  component of the MVC which displays information to the user.
   * @param model component of MVC which performs operations on data.
   * @param in    the source of input for the application.
   */
  public PortfolioStockController(PortfolioStockView view, PortfolioStockModel model, Readable in) {
    super(view, model, in);
  }
}
