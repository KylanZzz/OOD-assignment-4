package stock.controller.commands.portfolio;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import stock.controller.commands.stock.AddStock;
import stock.controller.commands.stock.PortfolioValue;
import stock.controller.commands.stock.RemoveStock;
import stock.controller.commands.stock.StockCommand;
import stock.model.StockModel;
import stock.view.BasicMenuOptions;
import stock.view.StockView;

/**
 * Command that prompts the user for how they want
 * to edit a portfolio.
 */
public class EditPortfolio extends PortfolioCommand {
  protected final String portfolioName;
  private final Map<String, StockCommand> commands;

  /**
   * Constructs a new edit portfolio command.
   * @param view the view of the stock program.
   * @param model the model of the stock program.
   * @param scanner the input of the stock program.
   * @param portfolioName name of the portfolio to edit.
   */
  public EditPortfolio(StockView view, StockModel model, Scanner scanner, String portfolioName) {
    super(view, model, scanner);
    this.portfolioName = portfolioName;
    commands = new HashMap<>();
  }

  /**
   * Initializes different commands that can be performed on the portfolio.
   */
  protected void initializeCommands() {
    commands.put("1", new PortfolioValue(view, model, scanner, portfolioName));
    commands.put("2", new AddStock(view, model, scanner, portfolioName));
    commands.put("3", new RemoveStock(view, model, scanner, portfolioName));
  }

  /**
   * Repeatedly asks the user for the command they want to perform on the
   * portfolio until the user inputs the exit keyword.
   */
  @Override
  public void apply() {
    initializeCommands();
    String choice = "";

    while (!choice.equals(BasicMenuOptions.exitKeyword())) {
      view.printManagePortfolio(model.getPortfolioContents(portfolioName), portfolioName);
      choice = scanner.nextLine();

      if (commands.containsKey(choice)) {
        commands.get(choice).apply();
      } else if (!choice.equals(BasicMenuOptions.exitKeyword())){
        view.printMessage("Invalid input. Please enter a valid choice (a number from 1 through "
                + BasicMenuOptions.managePortfolio().size() + ") or " + BasicMenuOptions.exitKeyword()
                + " to go back.");
      }
    }
  }
}
