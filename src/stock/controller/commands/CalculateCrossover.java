package stock.controller.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.model.StockModel;
import stock.view.StockView;

/**
 * Command to calculate and display the x-day crossovers for a specified stock.
 * An x-day crossover is when the closing price of a stock at that day is greater
 * than the moving average for that stock over the last x days.
 */
public class CalculateCrossover extends Command {

  /**
   * Constructs a calculate crossover command with a stock's view,
   * model, and source of input.
   *
   * @param view the view of the stock program.
   * @param model the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public CalculateCrossover(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
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
      var crossOvers = model.getCrossover(endDate, days, ticker);
      view.printXDayCrossovers(ticker, endDate, days, crossOvers);
    } catch (IOException e) {
      view.printMessage("Error while fetching data: " + e.getMessage());
    }
  }
}
