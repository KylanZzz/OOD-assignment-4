package stock.controller.commands;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

import stock.controller.commands.portfolio.CreatePortfolio;
import stock.controller.commands.portfolio.DeletePortfolio;
import stock.controller.commands.portfolio.EditPortfolio;
import stock.controller.commands.portfolio.PortfolioCommand;
import stock.controller.commands.portfolio.RenamePortfolio;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.BasicMenuOptions;
import stock.view.StockView;

/**
 * This command displays all the user's current portfolios to them,
 * as well as any actions that can be performed on them such as
 * creating a new portfolio, deleting an old one, or renaming a
 * portfolio.
 */
public class ViewPortfolios extends Command {

  private final Map<String, PortfolioCommand> commands;

  /**
   * Constructs a ViewPortfolios command with the given view, model, and scanner,
   * then initializes the commands map.
   *
   * @param view    the view to be used for displaying messages
   * @param model   the model to interact with stock data
   * @param scanner the scanner to read user inputs
   */
  public ViewPortfolios(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
    commands = new HashMap<>();
  }

  /**
   * Initializes commands for portfolio management: including creating, deleting,
   * renaming, and editing portfolios. The user should input 1 to create a new
   * portfolio, 2 to delete an exiting portfolio, 3 to rename an existing portfolio,
   * any number afterwards (4+) to edit any portfolios they own.
   */
  protected void initializeCommands() {
    commands.put("1", new CreatePortfolio(view, model, scanner));
    commands.put("2", new DeletePortfolio(view, model, scanner));
    commands.put("3", new RenamePortfolio(view, model, scanner));

    int numOptions = BasicMenuOptions.viewPortfolios().size();
    for (int i = 0; i < model.getPortfolios().size(); i++) {
      commands.put(Integer.toString(i + numOptions + 1),
              new EditPortfolio(view, model, scanner, model.getPortfolios().get(i)));
    }
  }

  /**
   * Executes the command. The user is continually prompted to select an option,
   * which executes the corresponding command, until the user inputs the exit
   * keyword.
   */
  @Override
  public void apply() {
    String choice = "";

    while (!choice.equals(BasicMenuOptions.exitKeyword())) {
      initializeCommands();
      view.printViewPortfolios(model.getPortfolios());
      choice = scanner.nextLine();

      if (commands.containsKey(choice)) {
        commands.get(choice).apply();
      } else if (!choice.equals(BasicMenuOptions.exitKeyword())) {
        view.printMessage("Invalid input. Please enter a valid choice (a number from 1 through "
                + BasicMenuOptions.viewPortfolios().size() + ") or " +
                BasicMenuOptions.exitKeyword() + " to go back.");
      }
    }
  }
}