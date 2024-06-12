package stock.controller.commands.portfolioStock;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import stock.controller.commands.ViewPortfolios;
import stock.controller.commands.portfolio.EditPortfolio;
import stock.controller.commands.portfolioStock.AdvancePortfolio.CreatePortfolio;
import stock.controller.commands.portfolioStock.AdvancePortfolio.DeletePortfolio;
import stock.controller.commands.portfolioStock.AdvancePortfolio.RenamePortfolio;
import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.BasicMenuOptions;
import stock.view.BasicPortfolioMenuOptions;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class ViewAdvancePortfolios extends PortfolioCommand {
  private final Map<String, PortfolioCommand> commands;

  /**
   * Constructs a ViewPortfolios command with the given view, model, and scanner,
   * then initializes the commands map.
   *
   * @param portfolioView    the view to be used for displaying messages
   * @param portfolioModel   the model to interact with stock data
   * @param scanner the scanner to read user inputs
   */
  public ViewAdvancePortfolios(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(portfolioView, portfolioModel, scanner);
    commands = new HashMap<>();
  }


  /**
   * Initializes commands for portfolio management: including creating, deleting,
   * renaming, and editing portfolios. The user should input 1 to create a new
   * portfolio, 2 to delete an exiting portfolio, 3 to rename an existing portfolio,
   * any number afterwards (4+) to edit any portfolios they own.
   */
  protected void initializeCommands() {
    commands.put("1", new CreatePortfolio(portfolioView, portfolioModel, scanner));
    commands.put("2", new DeletePortfolio(portfolioView, portfolioModel, scanner));
    commands.put("3", new RenamePortfolio(portfolioView, portfolioModel, scanner));

    int numOptions = BasicPortfolioMenuOptions.viewPortfolios().size();
    for (int i = 0; i < portfolioModel.getPortfolios().size(); i++) {
      commands.put(Integer.toString(i + numOptions + 1),
              new EditAdvancePortfolio(portfolioView, portfolioModel, scanner, portfolioModel.getPortfolios().get(i)));
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

    while (!choice.equals(BasicPortfolioMenuOptions.exitKeyword())) {
      initializeCommands();
      portfolioView.printViewPortfolios(portfolioModel.getPortfolios());
      choice = scanner.nextLine();

      if (commands.containsKey(choice)) {
        commands.get(choice).apply();
      } else if (!choice.equals(BasicPortfolioMenuOptions.exitKeyword())) {
        portfolioView.printMessage("Invalid input. Please enter a valid choice (a number from 1 through "
                + BasicPortfolioMenuOptions.viewPortfolios().size() + ") or " +
                BasicPortfolioMenuOptions.exitKeyword() + " to go back.");
      }
    }
  }
}
