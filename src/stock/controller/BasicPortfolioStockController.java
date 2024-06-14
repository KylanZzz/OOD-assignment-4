package stock.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import stock.controller.commands.CalculateAverage;
import stock.controller.commands.CalculateCrossover;
import stock.controller.commands.CalculateGain;
import stock.controller.commands.Command;
import stock.controller.commands.ViewPortfolios;
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
    commands.put("1", new CalculateGain(portfolioView, portfolioModel, scanner));
    commands.put("2", new CalculateAverage(portfolioView, portfolioModel, scanner));
    commands.put("3", new CalculateCrossover(portfolioView, portfolioModel, scanner));
    commands.put("4", new ViewPortfolios(portfolioView, portfolioModel, scanner));
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
