package stock.controller.commands.portfolioStock;

import java.io.IOException;
import java.util.List;
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

  protected final String getPortfolioNameFromUser() {
    while (true) {
      String name = scanner.nextLine().toUpperCase();

      try {
        portfolioModel.createNewPortfolioSave(name);
        portfolioView.printMessage(String.format("You selected:  %s: ", name));

      } catch (IOException e) {
        portfolioView.printMessage("Error while fetching data: " + e.getMessage());
      }
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
