package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.util.Scanner;

import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class CreatePortfolio extends PortfolioCommand {
  /**
   * Constructs a create portfolio command with a stock's
   * view, model, and source of input.
   *
   * @param portfolioView    the view of the stock program.
   * @param portfolioModel   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public CreatePortfolio(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(portfolioView, portfolioModel, scanner);
  }

  @Override
  public void apply() {
    portfolioView.printMessage("What is the name of the portfolio you would like to create?");
    String name = scanner.nextLine().toUpperCase();
    if (portfolioModel.getPortfolios().contains(name)) {
      portfolioView.printMessage("A portfolio with that name already exists!");
      return;
    }

    portfolioModel.createNewPortfolio(name);
    portfolioView.printMessage(String.format("Successfully created portfolio %s.", name));
  }
}
