package stock.controller.commands.AdvancePortfolio;

import java.util.Scanner;

import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.view.StockView;

public class LoadPortfolio extends StockCommand {
  public LoadPortfolio(StockView view, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(view, portfolioModel, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    view.printMessage("Where is the list of the portfolios that you have saved: ");
    
  }
}
