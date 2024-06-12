package stock.controller.commands.portfolioStock;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import stock.controller.StockController;
import stock.controller.commands.Command;
import stock.model.PortfolioStockModel;
import stock.view.BasicPortfolioMenuOptions;
import stock.view.PortfolioStockView;

public class BasicPortfolioStockController implements StockController {
  private  PortfolioStockModel portfolioModel;
  private PortfolioStockView portfolioView;
  private final Scanner scanner;
  private final Map<String, Command> commands;
  public BasicPortfolioStockController(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Readable in) {
    scanner = new Scanner(in);
    this.portfolioView = portfolioView;
    this.portfolioModel = portfolioModel;
    commands = new HashMap<>();
    initializePortfolioCommands();
  }

  protected void initializePortfolioCommands() {
    commands.put("1", new stock.controller.commands.portfolioStock.AdvancePortfolio.CalculateGain(portfolioView, portfolioModel, scanner));
    commands.put("2", new stock.controller.commands.portfolioStock.AdvancePortfolio.CalculateAverage(portfolioView, portfolioModel, scanner));
    commands.put("3", new stock.controller.commands.portfolioStock.AdvancePortfolio.CalculateCrossover(portfolioView, portfolioModel, scanner));
    commands.put("4", new ViewAdvancePortfolios(portfolioView, portfolioModel, scanner));
  }

  /**
   * Starts the stock program.
   */
  @Override
  public void run() {
    String choice = "";

    while (!choice.equals(BasicPortfolioMenuOptions.exitKeyword())) {
      portfolioView.printMainMenu();
      choice = scanner.nextLine();

      if (commands.containsKey(choice)) {
        commands.get(choice).apply();
      } else if (!choice.equals(BasicPortfolioMenuOptions.exitKeyword())) {

        portfolioView.printMessage("Invalid input. Please enter a valid choice (a number "
                + "from 1 through "
                + BasicPortfolioMenuOptions.mainMenu().size() + ") or " + BasicPortfolioMenuOptions.exitKeyword()
                + " to exit the application.");
      }
    }
  }
}
