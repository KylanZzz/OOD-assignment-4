package stock.controller.commands.portfolioStock;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import stock.controller.commands.Command;
import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class PortfolioCommand extends Command {
  public PortfolioCommand(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(portfolioView, portfolioModel, scanner);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {

  }

  @Override
  protected final String getTickerFromUser() {
    while (true) {
      String ticker = scanner.nextLine().toUpperCase();
      try {
        if (portfolioModel.stockExists(ticker)) {
          return ticker;
        } else {
          portfolioView.printMessage("That stock does not exist! Please try again.");
        }
      } catch (IOException e) {
        portfolioView.printMessage("Error while fetching data: " + e.getMessage());
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
      portfolioView.printMessage("Please enter the ticker: ");
      String ticker = scanner.nextLine().toUpperCase();

      portfolioView.printMessage("Please enter the proportion for this stock (Could be fractional): ");
      while (!scanner.hasNextDouble()) {
        portfolioView.printMessage("Invalid input. Please enter a numeric value for the proportion.");
        scanner.next();
      }
      double weight = scanner.nextDouble();
      scanner.nextLine();

      proportions.put(ticker, weight);

      portfolioView.printMessage("Please enter 'OK' when you finish or 'EDIT' to keep editing: ");
      String input = scanner.nextLine().toUpperCase().trim();

      if (input.equals("OK")) {
        return proportions;
      } else if (!input.equals("EDIT")) {
        portfolioView.printMessage("Unrecognized command. Please enter 'OK' to finish or 'EDIT' to continue editing.");
      }
    }
  }

  protected final String getPortfolioNameFromUser() {
    while (true) {
      String name = scanner.nextLine().toUpperCase();

      portfolioView.printMessage(String.format("You selected:  %s: ", name));
      return name;
    }
  }

  protected final String getPortfolioFileSaveName() {
    portfolioView.printMessage("Please enter the name of the portfolio that you want to fetch: ");
    String name = scanner.nextLine().toUpperCase();
    List<String> PortfolioList = portfolioModel.getPortfolios();
    if (PortfolioList.contains(name)) {
      return name;
    } else {
//      view.printMessage("Name of that portfolio doesn't exist");
      throw new IllegalArgumentException("Name of that portfolio doesn't exist");
    }
  }

  protected final String getFileSaves() {
    portfolioView.printMessage("Please select the file in this portfolio: ");
    String name = scanner.nextLine().toUpperCase();
    return name;
  }
}
