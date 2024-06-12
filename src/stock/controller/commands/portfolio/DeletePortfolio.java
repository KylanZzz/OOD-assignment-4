package stock.controller.commands.portfolio;

import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.StockView;

/**
 * Command to delete an existing portfolio.
 */
public class DeletePortfolio extends PortfolioCommand {

  /**
   * Constructs a delete portfolio command with a stock's view,
   * model, and source of input.
   *
   * @param view the view of the stock program.
   * @param model the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public DeletePortfolio(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
  }

  /**
   * Prompts the user for the name of the portfolio they want to delete, then
   * creates the portfolio and informs the user.
   * If there is not a portfolio with such a name, then an error is displayed
   * and the command is terminated.
   */
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
