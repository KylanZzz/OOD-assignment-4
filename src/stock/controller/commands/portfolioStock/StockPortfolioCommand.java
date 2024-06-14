package stock.controller.commands.portfolioStock;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import stock.controller.commands.Command;
import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
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

  protected final String getTickerInPortfolioFromUser(LocalDate date) {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;

    while (true) {
      String ticker = scanner.nextLine().toUpperCase();
      if (portfolioModel.getPortfolioContentsDecimal(portfolio, date).containsKey(ticker)) {
        return ticker;
      } else {
        portfolioView.printMessage("Please enter the stock that you bought before that date: ");
      }
    }
  }

  protected final String getPortfolioNameFromUserWOSave() {
    String name = scanner.nextLine().toUpperCase();
     return name;
  }

  protected final Map<String, Double> getProportionsFromUser(LocalDate date) {
    PortfolioStockView portfolioView = (PortfolioStockView) view;
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    Map<String, Double> proportions = new HashMap<>();
    double counter = 0;
    int portfolioSize = portfolioModel.getPortfolioContentsDecimal(portfolio, date).size();

    portfolioView.printManagePortfolioDouble(portfolioModel.getPortfolioContentsDecimal(portfolio, date), portfolio);
    for (int i = 0; i < portfolioSize; i++) {

      portfolioView.printMessage("Please enter the ticker you have not entered before: ");
      String ticker = getTickerInPortfolioFromUser(date);

      portfolioView.printMessage("Make sure that the proportion of all stocks can be add up to 1.00! ");
      portfolioView.printMessage("Please enter the proportion for this stock in the decimal format: ");
      while (!scanner.hasNextDouble()) {
        portfolioView.printMessage("Invalid input. Please enter a numeric value for the proportion.");
        scanner.next();
      }

      double weight = scanner.nextDouble();
      scanner.nextLine();

      while (weight > 1.0 || counter > 1.0) {
        portfolioView.printMessage("Invalid input. Please enter a value that will not make over 1.00: ");
        weight = scanner.nextDouble();
        scanner.nextLine();
      }

      counter += weight;

      if (i == portfolioSize - 1) {
        while(counter != 1.00) {
          counter -= weight;
          portfolioView.printMessage("The proportion of the last stock did not add up to 1.00, "
                  + "pleas enter the proportion for the last ticker again: ");
          weight = scanner.nextDouble();
          scanner.nextLine();
          counter += weight;
        }
      }

      proportions.put(ticker, weight);
    }
    return proportions;
  }

  protected final String getPortfolioNameFromUser() {
    while (true) {
      String name = scanner.nextLine().toUpperCase();

      view.printMessage(String.format("You selected:  %s: ", name));
      return name;
    }
  }

  protected final String getPortfolioFileSaveName() {
    PortfolioStockView portfolioView = (PortfolioStockView) view;
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;

    int option = -1;
    List<String> PortfolioList = new ArrayList<>();
    try {
      PortfolioList = portfolioModel.getPortfolioSaves(portfolio);
      option = getPositiveFromUser(PortfolioList.size());
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
      ;
    }
    return PortfolioList.get(option - 1);
  }

  protected final String getFileSavesName() {
    view.printMessage("Please select the file in this portfolio: ");
    String name = scanner.nextLine().toUpperCase();
    return name;
  }
}
