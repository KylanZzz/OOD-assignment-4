package stock.controller.commands.ps;

import java.util.Scanner;

import stock.controller.commands.portfolio.EditPortfolio;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.BasicPortfolioStockView;
import stock.view.StockView;

public class PEditPortfolio extends EditPortfolio {
  /**
   * Constructs a new edit portfolio command.
   *
   * @param view          the view of the stock program.
   * @param model         the model of the stock program.
   * @param scanner       the input of the stock program.
   * @param portfolioName name of the portfolio to edit.
   */
  public PEditPortfolio(BasicPortfolioStockView view, PortfolioStockModel model, Scanner scanner, String portfolioName) {
    super(view, model, scanner, portfolioName);
  }
}
