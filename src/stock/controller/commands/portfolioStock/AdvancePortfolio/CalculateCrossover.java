package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.StockView;

public class CalculateCrossover extends PortfolioCommand {
  public CalculateCrossover(StockView view, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(view, portfolioModel, scanner);
  }

  /**
   * Executes the command. Prompts the user for the stock ticker, the end date,
   * and the number of days to calculate the x-day crossover.
   * If the user inputs an invalid ticker, an error message is displayed and
   * the user is prompted again.
   * If the user inputs an invalid date format or a future date, an error message
   * is displayed and the user is prompted again.
   * If an error occurs while fetching data from the model, an error message is
   * displayed.
   */
  @Override
  public void apply() {
    view.printMessage("Please enter the ticker of the stock that you would like to know about:");
    String ticker = getTickerFromUser();

    view.printMessage("Please enter the ending date in the format MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    view.printMessage("Please enter the number of days.");
    int days = getPositiveFromUser(Integer.MAX_VALUE);

    try {
      var crossOvers = portfolioModel.getCrossover(endDate, days, ticker);
      view.printXDayCrossovers(ticker, endDate, days, crossOvers);
    } catch (IOException e) {
      view.printMessage("Error while fetching data: " + e.getMessage());
    }
  }
}
