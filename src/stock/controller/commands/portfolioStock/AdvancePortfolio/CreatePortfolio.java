package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.util.Scanner;

import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.StockView;

public class CreatePortfolio extends PortfolioCommand {
  /**
   * Constructs a create portfolio command with a stock's
   * view, model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param portfolioModel   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public CreatePortfolio(StockView view, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(view, portfolioModel, scanner);
  }

  @Override
  public void apply() {
    view.printMessage("What is the name of the portfolio you would like to create?");
    String name = scanner.nextLine().toUpperCase();
    if (portfolioModel.getPortfolios().contains(name)) {
      view.printMessage("A portfolio with that name already exists!");
      return;
    }

    portfolioModel.createNewPortfolio(name);
    view.printMessage(String.format("Successfully created portfolio %s.", name));
  }
}
