package stock.controller;

import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;

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
