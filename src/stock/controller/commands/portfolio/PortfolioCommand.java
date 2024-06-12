package stock.controller.commands.portfolio;

import java.util.Scanner;

import stock.controller.commands.Command;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.StockView;

/**
 * Represents a command that alters the stock portfolios in some way.
 * This involves editing, creating, deleting, etc.
 */
public abstract class PortfolioCommand extends Command {
  public PortfolioCommand(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
  }

}
