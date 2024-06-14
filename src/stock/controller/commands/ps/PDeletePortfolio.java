package stock.controller.commands.ps;

import java.util.Scanner;

import stock.controller.commands.portfolio.DeletePortfolio;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.BasicPortfolioStockView;
import stock.view.StockView;

public class PDeletePortfolio extends DeletePortfolio {
  /**
   * Constructs a delete portfolio command with a stock's view,
   * model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param model   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public PDeletePortfolio(BasicPortfolioStockView view, PortfolioStockModel model, Scanner scanner) {
    super(view, model, scanner);
  }
}
