package stock.controller;

import java.util.HashMap;
import java.util.Scanner;
import stock.controller.commands.CalculateAverage;
import stock.controller.commands.CalculateCrossover;
import stock.controller.commands.CalculateGain;
import stock.controller.commands.Command;
import stock.controller.commands.ViewPortfolios;
import stock.model.StockModel;
import stock.view.BasicPortfolioMenuOptions;
import stock.view.StockView;
import java.util.Map;

/**
 * A simple implementation of a stock controller. It allows the user to analyze
 * stocks such as finding the x-day average, gain/loss over a period of time,
 * as well as creating portfolios and analyzing their value through text-based
 * input.
 */
public class BasicStockController implements StockController {

  private  StockView view;
  private  StockModel model;
  private final Scanner scanner;
  private final Map<String, Command> commands;

  /**
   * Constructs a BasicStockController with the given view, model, and input
   * source.
   *
   * @param view component of the MVC which displays information to the user.
   * @param model component of MVC which performs operations on data.
   * @param in the source of input for the application.
   */
  public BasicStockController(StockView view, StockModel model, Readable in) {
    scanner = new Scanner(in);
    this.view = view;
    this.model = model;
    commands = new HashMap<>();
    initializeCommands();
  }

  protected void initializeCommands() {
    commands.put("1", new CalculateGain(view, model, scanner));
    commands.put("2", new CalculateAverage(view, model, scanner));
    commands.put("3", new CalculateCrossover(view, model, scanner));
    commands.put("4", new ViewPortfolios(view, model, scanner));
  }


  /**
   * Starts the stock application. Displays the main menu, reads user input,
   * and executes the commands until the user exits the application.
   */
  @Override
  public void run() {
    String choice = "";

    while (!choice.equals(BasicPortfolioMenuOptions.exitKeyword())) {
      view.printMainMenu();
      choice = scanner.nextLine();

      if (commands.containsKey(choice)) {
        commands.get(choice).apply();
      } else if (!choice.equals(BasicPortfolioMenuOptions.exitKeyword())) {
        view.printMessage("Invalid input. Please enter a valid choice or "
                + BasicPortfolioMenuOptions.exitKeyword()
                + " to exit the application.");
      }
    }
  }
}
