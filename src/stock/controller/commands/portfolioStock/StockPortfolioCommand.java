package stock.controller.commands.portfolioStock;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import stock.controller.commands.Command;
import stock.controller.commands.stock.StockCommand;
import stock.model.StockModel;
import stock.view.StockView;

public class StockPortfolioCommand extends StockCommand {
  public StockPortfolioCommand(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  @Override
  public void apply() {

  }

  /**
   * Executes the command.
   */

  @Override
  protected final String getTickerFromUser() {
    while (true) {
      String ticker = scanner.nextLine().toUpperCase();
      try {
        if (model.stockExists(ticker)) {
          return ticker;
        } else {
          view.printMessage("That stock does not exist! Please try again.");
        }
      } catch (IOException e) {
        view.printMessage("Error while fetching data: " + e.getMessage());
      }
    }
  }

  protected final String getPortfolioNameFromUserWOSave() {
    String name = scanner.nextLine().toUpperCase();
     return name;
  }

  protected final Map<String, Double> getProportionsFromUser() {
    Map<String, Double> proportions = new HashMap<>();
    while (true) {
      view.printMessage("Please enter the ticker: ");
      String ticker = scanner.nextLine().toUpperCase();

      view.printMessage("Please enter the proportion for this stock (Could be fractional): ");
      while (!scanner.hasNextDouble()) {
        view.printMessage("Invalid input. Please enter a numeric value for the proportion.");
        scanner.next();
      }
      double weight = scanner.nextDouble();
      scanner.nextLine();

      proportions.put(ticker, weight);

      view.printMessage("Please enter 'OK' when you finish or 'EDIT' to keep editing: ");
      String input = scanner.nextLine().toUpperCase().trim();

      if (input.equals("OK")) {
        return proportions;
      } else if (!input.equals("EDIT")) {
        view.printMessage("Unrecognized command. Please enter 'OK' to finish or 'EDIT' to continue editing.");
      }
    }
  }

  protected final String getPortfolioNameFromUser() {
    while (true) {
      String name = scanner.nextLine().toUpperCase();

      view.printMessage(String.format("You selected:  %s: ", name));
      return name;
    }
  }

  protected final String getPortfolioFileSaveName() {
//    portfolioView.printMessage("please enter the option by the number: ");
    int option = scanner.nextInt();
//    String name = scanner.nextLine().toUpperCase();
    List<String> PortfolioList = model.getPortfolios();
      return PortfolioList.get(option - 1);
  }

  protected final String getFileSaves() {
    view.printMessage("Please select the file in this portfolio: ");
    String name = scanner.nextLine().toUpperCase();
    return name;
  }
}
