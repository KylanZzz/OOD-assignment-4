package stock.controller.commands.ps;

import java.util.Scanner;

import stock.controller.commands.portfolio.RenamePortfolio;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.BasicPortfolioStockView;
import stock.view.StockView;

public class PRenamePortfolio extends RenamePortfolio {
  /**
   * Constructs a rename portfolio command with a stock's view,
   * model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param model   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public PRenamePortfolio(BasicPortfolioStockView view, PortfolioStockModel model, Scanner scanner) {
    super(view, model, scanner);
  }
}
