package stock.controller.commands.portfolio;

import java.util.Scanner;

import stock.model.StockModel;
import stock.view.StockView;

public class RenamePortfolio extends PortfolioCommand {
  public RenamePortfolio(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
  }

  @Override
  public void apply() {
    view.printMessage("What portfolio would you like to rename? (Please enter the name).");
    String oldName = scanner.nextLine().toUpperCase();
    if (!model.getPortfolios().contains(oldName)) {
      view.printMessage("A portfolio with that name does not exist!");
      return;
    }

    view.printMessage("What would you like to rename this portfolio to?");
    String newName = scanner.nextLine().toUpperCase();
    if (!model.getPortfolios().contains(oldName)) {
      view.printMessage("A portfolio with that name does not exist!");
      return;
    }

    if (model.getPortfolios().contains(newName)) {
      view.printMessage("A portfolio with that name already exists!");
    } else {
      model.renamePortfolio(oldName, newName);
      view.printMessage(String.format("Successfully renamed portfolio %s to %s.", oldName, newName));
    }
  }
}
