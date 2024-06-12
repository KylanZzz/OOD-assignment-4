package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.util.Scanner;

import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.StockView;

public class DeletePortfolio extends PortfolioCommand {
  public DeletePortfolio(StockView view, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(view, portfolioModel, scanner);
  }

  @Override
  public void apply() {
    view.printMessage("What portfolio would you like to delete?");
    String name = scanner.nextLine().toUpperCase();
    if (!portfolioModel.getPortfolios().contains(name)) {
      view.printMessage("A portfolio with that name does not exist!");
      return;
    }

    portfolioModel.deletePortfolio(name);
    view.printMessage(String.format("Successfully deleted portfolio %s.", name));
  }
}
