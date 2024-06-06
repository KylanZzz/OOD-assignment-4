package stock.controller.commands.portfolio;

import java.util.Scanner;

import stock.model.StockModel;
import stock.view.StockView;

/**
 * Command to create a new empty stock portfolio.
 */
public class CreatePortfolio extends PortfolioCommand {

  /**
   * Constructs a create portfolio command with a stock's
   * view, model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param model   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public CreatePortfolio(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
  }

  /**
   * Prompts the user for the name of the portfolio they want to create, then
   * creates the portfolio and informs the user.
   * If there is already a portfolio with such a name, then an error is displayed
   * and the command is terminated.
   */
  @Override
  public void apply() {
    view.printMessage("What is the name of the portfolio you would like to create?");
    String name = scanner.nextLine().toUpperCase();
    if (model.getPortfolios().contains(name)) {
      view.printMessage("A portfolio with that name already exists!");
      return;
    }

    model.createNewPortfolio(name);
    view.printMessage(String.format("Successfully created portfolio %s.", name));
  }
}
