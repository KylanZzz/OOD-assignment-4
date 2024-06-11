package stock.controller.commands.stock;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.StockView;

public class AddStockWithDate extends StockCommand {
  /**
   * Constructs an add stock command with a stock's view,
   * model, and source of input.
   *
   * @param view      the view of the stock program.
   * @param model     the model of the stock program.
   * @param scanner   the input of the stock program.
   * @param portfolio the name of the portfolio.
   */
  public AddStockWithDate(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);

  }

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

    view.printMessage("What specified date of the stocks you want to buy: ");

    LocalDate date = getDateFromUser();

    model.addStockToPortfolio(portfolio, ticker, shares, date);


    view.printMessage(String.format("Successfully purchased %d number of %s stocks in the %s "
            + "portfolio.", shares, ticker, portfolio));

  }
}
