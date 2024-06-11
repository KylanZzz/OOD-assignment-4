package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.util.Scanner;

import stock.controller.commands.Command;
import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.StockView;

public abstract class StockPortfolioCommand extends PortfolioCommand {

  protected final String portfolio;

  public StockPortfolioCommand(StockView view, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(view, portfolioModel, scanner);
    this.portfolio = portfolio;
  }
}
