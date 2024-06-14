package stock.controller.commands.ps;

import java.util.Scanner;

import stock.controller.commands.portfolio.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.BasicPortfolioStockView;
import stock.view.StockView;

public class PPortfolioCommand extends PortfolioCommand {
  public PPortfolioCommand(BasicPortfolioStockView view, PortfolioStockModel model, Scanner scanner) {
    super(view, model, scanner);
  }

  @Override
  public void apply() {

  }
}
