package stock.controller.commands.stock;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import stock.model.StockModel;
import stock.view.StockView;

public class AddStock extends StockCommand {
  public AddStock(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  @Override
  public void apply() {
    view.printMessage(String.format("Please enter the ticker of the stock " +
            "that you would like to add to portfolio %s:", portfolio));
    String ticker = getTickerFromUser();

    view.printMessage("Please enter the number of shares you would like to " +
            "purchase (you cannot buy fractional number of stocks): ");
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
    view.printMessage(String.format("Successfully purchased %d number of %s stocks in the %s " +
            "portfolio.", shares, ticker, portfolio));
  }
}
