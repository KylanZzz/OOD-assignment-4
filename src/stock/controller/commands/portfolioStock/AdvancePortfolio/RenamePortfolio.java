package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.util.Scanner;

import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.StockView;

public class RenamePortfolio extends PortfolioCommand {
  public RenamePortfolio(StockView view, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(view, portfolioModel, scanner);
  }

  @Override
  public void apply() {
    view.printMessage("What portfolio would you like to rename? (Please enter the name).");
    String oldName = scanner.nextLine().toUpperCase();
    if (!portfolioModel.getPortfolios().contains(oldName)) {
      view.printMessage("A portfolio with that name does not exist!");
      return;
    }

    view.printMessage("What would you like to rename this portfolio to?");
    String newName = scanner.nextLine().toUpperCase();

    if (portfolioModel.getPortfolios().contains(newName)) {
      view.printMessage("A portfolio with that name already exists!");
      return;
    }

    portfolioModel.renamePortfolio(oldName, newName);
    view.printMessage(String.format("Successfully renamed portfolio %s to %s.", oldName, newName));
  }
}
