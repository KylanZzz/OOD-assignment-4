package stock.controller.commands.portfolio;

import java.util.Scanner;

import stock.controller.commands.Command;
import stock.model.StockModel;
import stock.view.StockView;

public abstract class PortfolioCommand extends Command {
  public PortfolioCommand(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
  }
}
