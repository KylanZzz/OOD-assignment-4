package stock.controller.commands.stock;

import java.time.LocalDate;
import java.util.Scanner;

import stock.controller.commands.Command;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.StockView;

/**
 * Represents a command that can be performed on a portfolio.
 */
public abstract class StockCommand extends Command {
  protected final String portfolio;

  /**
   * Constructs a remove stock command with a stock's view,
   * model, and source of input.
   * @param view the view of the stock program.
   * @param model the model of the stock program.
   * @param scanner the input of the stock program.
   * @param portfolio the name of the portfolio.
   */
  public StockCommand(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner);
    this.portfolio = portfolio;
  }

  public StockCommand(StockView view, PortfolioStockModel model, Scanner scanner, String portfolio, LocalDate date) {
    super(view, model, scanner, date);
    this.portfolio = portfolio;
  }
}
