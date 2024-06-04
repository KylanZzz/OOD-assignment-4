package stock.controller.commands.portfolio;

import java.util.Scanner;

import stock.model.StockModel;
import stock.view.StockView;

public class DeletePortfolio extends PortfolioCommand {
  public DeletePortfolio(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
  }

  @Override
  public void apply() {
    view.printMessage("What portfolio would you like to delete?");
    String name = scanner.nextLine().toUpperCase();
    if (!model.getPortfolios().contains(name)) {
      view.printMessage("A portfolio with that name does not exist!");
      return;
    }

    model.deletePortfolio(name);
    view.printMessage(String.format("Successfully deleted portfolio %s.", name));
  }
}
