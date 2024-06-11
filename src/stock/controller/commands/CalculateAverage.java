package stock.controller.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.StockView;

/**
 * Command to calculate the average price of a stock over the last x-days.
 */
public class CalculateAverage extends Command {

  /**
   * Constructs a calculate average command with a stock's view, model,
   * and source of input.
   *
   * @param view the view of the stock program.
   * @param model the model of the stock program.
   * @param scanner the input of the stock program.
   */
//  public CalculateAverage(StockView view, StockModel model, Scanner scanner) {
//    super(view, model, scanner);
//  }
  public CalculateAverage(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
  }

  /**
   * Prompts the user for the date, stock ticker, and number of days, then calculates
   * and displays the the user the average price of the stock over the last x-days.
   * If the ticker is invalid, an error is displayed and the user is prompted again.
   * If the date is invalid, an error is displayed and the user is prompted again.
   * If the number of days is invalid (non-positive or not an integer) then an error
   * is displayed and the user is prompted again.
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
      double average = model.getMovingDayAverage(endDate, days, ticker);
      view.printStockAverage(ticker, endDate, days, average);
    } catch (IOException e) {
      view.printMessage("Error while fetching data: " + e.getMessage());
    }
  }
}
