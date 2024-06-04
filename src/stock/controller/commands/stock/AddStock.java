package stock.controller.commands.stock;

import java.io.IOException;
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
    String ticker = scanner.nextLine().toUpperCase();
    try {
      if (!model.stockExists(ticker)) {
        view.printMessage("That stock does not exist!");
        return;
      }
    } catch (IOException e) {
      view.printMessage("Error while fetching data: " + e.getMessage());
    }

    view.printMessage("Please enter the number of shares you would like to " +
            "purchase (you cannot buy fractional number of stocks): ");
    int shares = scanner.nextInt();
    scanner.nextLine();
    if (shares == 0) {
      view.printMessage("Cannot purchase 0 shares of a stock.");
      return;
    } if (shares < 0) {
      view.printMessage("Cannot purchase negative number of stocks.");
      return;
    }

    if (model.getPortfolioContents(portfolio).containsKey(ticker)) {
      model.addStockToPortfolio(portfolio, ticker, shares);
      view.printMessage(String.format("Successfully purchased %d number of %s stocks in the %s " +
              "portfolio.", shares, ticker, portfolio));
    } else {
      model.addStockToPortfolio(portfolio, ticker, shares);
      view.printMessage(String.format("Successfully added stock %s to portfolio %s.", ticker, portfolio));
    }

  }
}
