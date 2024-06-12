package stock.controller.commands.stock;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.StockView;

/**
 * Command to calculate the value of a portfolio.
 */
public class PortfolioValue extends StockCommand {

  /**
   * Constructs a portfolio value command with a stock's view,
   * model, and source of input.
   *
   * @param view the view of the stock program.
   * @param model the model of the stock program.
   * @param scanner the input of the stock program.
   * @param portfolio the name of the portfolio.
   */
  public PortfolioValue(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  /**
   * Prompts the user for the date they want to calculate the
   * value of the portfolio at, then displays the value of the
   * portfolio.
   * If an invalid date is entered, an error message is displayed
   * and the user is prompted for a different date.
   */
  @Override
  public void apply() {
    view.printMessage(String.format("What date would you like to know the value of portfolio %s " +
            "at? Please enter the date in the format MM/DD/YYYY.", portfolio));
    LocalDate date = getDateFromUser();

    try {
      double value = model.getPortfolioValue(portfolio, date);
      view.printMessage(String.format("The value of the portfolio %s at %s is %.2f.",
              portfolio, date, value));
    } catch (IOException e) {
      view.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
