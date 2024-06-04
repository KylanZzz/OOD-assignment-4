package stock.controller.commands.stock;

import java.util.Scanner;

import stock.controller.commands.Command;
import stock.model.StockModel;
import stock.view.StockView;

public abstract class StockCommand extends Command {
  protected final String portfolio;

  public StockCommand(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner);
    this.portfolio = portfolio;
  }
}
