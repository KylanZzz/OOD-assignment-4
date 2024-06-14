package stock.controller.commands.portfolio;

import java.util.Scanner;
import stock.model.StockModel;
import stock.view.StockView;

/**
 * Command to change the name of a portfolio.
 */
public class RenamePortfolio extends PortfolioCommand {

  /**
   * Constructs a rename portfolio command with a stock's view,
   * model, and source of input.
   *
   * @param view the view of the stock program.
   * @param model the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public RenamePortfolio(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
  }

  /**
   * Asks the user for the name of the portfolio they'd like to rename, then
   * the new name of the portfolio.
   * If a portfolio with the name doesn't exist or if there is already a portfolio
   * with the new name, then an error is displayed and the command is terminated.
   */
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

    if (model.getPortfolios().contains(newName)) {
      view.printMessage("A portfolio with that name already exists!");
      return;
    }

    model.renamePortfolio(oldName, newName);
    view.printMessage(String.format("Successfully renamed portfolio %s to %s.", oldName, newName));
  }
}
