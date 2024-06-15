package stock.controller.commands.portfoliostock;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

/**
 * Abstract class providing base functionalities to interact with stock portfolios,
 * including common utility methods to fetch user inputs in a structured manner.
 */
public class StockPortfolioCommand extends StockCommand {

  /**
   * Constructs a StockPortfolioCommand with necessary context for portfolio operations.
   *
   * @param view     the interface for user interactions.
   * @param model    the data model for stock and portfolio operations.
   * @param scanner  the input scanner.
   * @param portfolio the portfolio name involved in the command.
   */
  public StockPortfolioCommand(StockView view, StockModel model,
                               Scanner scanner, String portfolio) {
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

  protected final Map<String, Double> getProportionsFromUser(LocalDate date) {
    PortfolioStockView portfolioView = (PortfolioStockView) view;
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    Map<String, Double> proportions = new HashMap<>();
    List<String> storedTicker = new ArrayList<>();
    double counter = 0;
    int portfolioSize = portfolioModel.getPortfolioContentsDecimal(portfolio, date).size();

    portfolioView.printManagePortfolioDouble(portfolioModel
            .getPortfolioContentsDecimal(portfolio, date), portfolio);
    for (int i = 0; i < portfolioSize; i++) {

      portfolioView.printMessage("Please enter the ticker you have not entered before: ");
      String ticker = getTickerInPortfolioFromUser(date);
      while (storedTicker.contains(ticker)) {
        portfolioView.printMessage("Please enter the ticker you have not entered before: ");
        ticker = getTickerInPortfolioFromUser(date);
      }
      storedTicker.add(ticker);


      portfolioView.printMessage("Make sure that the proportion "
              + "of all stocks can be add up to 1.00! ");
      portfolioView.printMessage("Please enter the proportion for "
              + "this stock in the decimal format: ");
      while (!scanner.hasNextDouble()) {
        portfolioView.printMessage("Invalid input. Please enter"
                + " a numeric value for the proportion.");
        scanner.next();
      }

      double weight = scanner.nextDouble();
      scanner.nextLine();

      while (weight > 1.0 || counter > 1.0) {
        portfolioView.printMessage("Invalid input. Please enter a "
                + "value that will not make over 1.00: ");
        weight = scanner.nextDouble();
        scanner.nextLine();
      }

      counter += weight;

      if (i == portfolioSize - 1) {
        while (counter != 1.00) {
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

  protected final String getPortfolioFileSaveName() {
    PortfolioStockView portfolioView = (PortfolioStockView) view;
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;

    int option = -1;
    List<String> portfolioList = new ArrayList<>();
    try {
      portfolioList = portfolioModel.getPortfolioSaves(portfolio);
      option = getPositiveFromUser(portfolioList.size());
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
      ;
    }
    return portfolioList.get(option - 1);
  }
}
