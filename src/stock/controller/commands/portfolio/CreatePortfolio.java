package stock.controller.commands.portfolio;

import java.util.Scanner;

import stock.model.StockModel;
import stock.view.StockView;

public class CreatePortfolio extends PortfolioCommand{
  public CreatePortfolio(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
  }

  @Override
  public void apply() {
    view.printMessage("What is the name of the portfolio you would like to create?");
    String name = scanner.nextLine().toUpperCase();
    if (model.getPortfolios().contains(name)) {
      view.printMessage("A portfolio with that name already exists!");
    }

    model.createNewPortfolio(name);
    view.printMessage(String.format("Successfully created portfolio %s.", name));
  }
}
