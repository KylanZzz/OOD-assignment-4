package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.util.Scanner;

import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class RenamePortfolio extends PortfolioCommand {
  public RenamePortfolio(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(portfolioView, portfolioModel, scanner);
  }

  @Override
  public void apply() {
    portfolioView.printMessage("What portfolio would you like to rename? (Please enter the name).");
    String oldName = scanner.nextLine().toUpperCase();
    if (!portfolioModel.getPortfolios().contains(oldName)) {
      portfolioView.printMessage("A portfolio with that name does not exist!");
      return;
    }

    portfolioView.printMessage("What would you like to rename this portfolio to?");
    String newName = scanner.nextLine().toUpperCase();

    if (portfolioModel.getPortfolios().contains(newName)) {
      portfolioView.printMessage("A portfolio with that name already exists!");
      return;
    }

    portfolioModel.renamePortfolio(oldName, newName);
    view.printMessage(String.format("Successfully renamed portfolio %s to %s.", oldName, newName));
  }
}
