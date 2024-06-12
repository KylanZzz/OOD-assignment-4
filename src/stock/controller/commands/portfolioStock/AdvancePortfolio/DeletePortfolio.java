package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.util.Scanner;

import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class DeletePortfolio extends PortfolioCommand {
  public DeletePortfolio(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(portfolioView, portfolioModel, scanner);
  }

  @Override
  public void apply() {
    portfolioView.printMessage("What portfolio would you like to delete?");
    String name = scanner.nextLine().toUpperCase();
    if (!portfolioModel.getPortfolios().contains(name)) {
      portfolioView.printMessage("A portfolio with that name does not exist!");
      return;
    }

    portfolioModel.deletePortfolio(name);
    portfolioView.printMessage(String.format("Successfully deleted portfolio %s.", name));
  }
}
