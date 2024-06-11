package stock.controller.commands.stock;

import java.util.InputMismatchException;
import java.util.Scanner;

import stock.model.StockModel;
import stock.view.StockView;

/**
 * Command to add a new stock (or add shares to an existing
 * stock) to a portfolio.
 */
public class AddStock extends StockCommand {

  /**
   * Constructs an add stock command with a stock's view,
   * model, and source of input.
   *
   * @param view the view of the stock program.
   * @param model the model of the stock program.
   * @param scanner the input of the stock program.
   * @param portfolio the name of the portfolio.
   */
  public AddStock(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  /**
   * Prompts the user to input the stock ticker and number of shares they
   * would like to add to the portfolio. If the stock is already present
   * in the portfolio, it will add the inputted number of shares to the
   * existing number of shares.
   * If an invalid ticker is entered, then it will re-prompt the user until
   * a valid input is given.
   * If an invalid number of shares is given (a non-whole number or non-
   * integer), then and error message is displayed and the command will
   * terminate.
   */
  @Override
  public void apply() {
    view.printMessage(String.format("Please enter the ticker of the stock "
            + "that you would like to add to portfolio %s:", portfolio));
    String ticker = getTickerFromUser();

    view.printMessage("Please enter the number of shares you would like to "
            + "purchase (you cannot buy fractional number of stocks): ");
    int shares;
    try {
      shares = scanner.nextInt();
      scanner.nextLine();
    } catch (InputMismatchException e) {
      view.printMessage("Invalid input: not an integer, please try again.");
      scanner.nextLine();
      return;
    }

    if (shares == 0) {
      view.printMessage("Cannot purchase 0 shares of a stock.");
      return;
    }

    if (shares < 0) {
      view.printMessage("Cannot purchase negative number of stocks.");
      return;
    }

    model.addStockToPortfolio(portfolio, ticker, shares);
    view.printMessage(String.format("Successfully purchased %d number of %s stocks in the %s "
            + "portfolio.", shares, ticker, portfolio));
  }
}