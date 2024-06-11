package stock.controller.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.StockView;

/**
 * This command calculates the gain/loss of a stock between two given dates.
 */
public class CalculateGain extends Command {

  /**
   * Constructs a calculate gain command with a stock's view,
   * model, and source of input.
   *
   * @param view the view of the stock program.
   * @param model the model of the stock program.
   * @param scanner the input of the stock program.
   */
//  public CalculateGain(StockView view, StockModel model, Scanner scanner) {
//    super(view, model, scanner);
//  }

  public CalculateGain(StockView view, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(view, portfolioModel, scanner);
  }

  /**
   * Executes the CalculateGain command. Prompts the user for the stock ticker,
   * start date, and end date, then calculates and displays the gain or loss
   * of the specified stock over the given period.
   * If the ticker symbol does not exist, prompts the user to try again.
   * If the date format is incorrect, prompts the user to enter the date in the correct format.
   * If the date values are invalid (e.g., non-existent date), prompts the user to enter a valid
   * date.
   * If the end date is before or the same as the start date, displays an error message and stops
   * the operation.
   * If an error occurs while fetching data, displays an error message.
   */
  @Override
  public void apply() {
    view.printMessage("Please enter the ticker of the stock that you would like to know about:");
    String ticker = getTickerFromUser();

    view.printMessage("Please enter the starting date (inclusive) in the format MM/DD/YYYY:");
    LocalDate startDate = getDateFromUser();

    view.printMessage("Please enter the ending date (inclusive) in the format MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    if (!endDate.isAfter(startDate)) {
      view.printMessage("Invalid input: The end date must be after the start date.");
      return;
    }

    try {
      double gain = portfolioModel.getGainOverTime(startDate, endDate, ticker);
      view.printStockGain(ticker, startDate, endDate, gain);
    } catch (IOException e) {
      view.printMessage("Error while fetching data: " + e.getMessage());
    }
  }
}
