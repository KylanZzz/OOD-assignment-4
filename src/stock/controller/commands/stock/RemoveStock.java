package stock.controller.commands.stock;

import java.io.IOException;
import java.util.Scanner;

import stock.model.StockModel;
import stock.view.StockView;

public class RemoveStock extends StockCommand {
  public RemoveStock(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  @Override
  public void apply() {
    view.printMessage(String.format("Please enter the ticker of the stock " +
            "that you would like to remove from portfolio %s.", portfolio));
    String ticker = scanner.nextLine().toUpperCase();
    try {
      if (!model.stockExists(ticker)) {
        view.printMessage("That stock does not exist!");
        return;
      }
    } catch (IOException e) {
      view.printMessage("Error while fetching data: " + e.getMessage());
    }

    if (!model.getPortfolioContents(portfolio).containsKey(ticker)) {
      view.printMessage("That stock is not in the portfolio.");
      return;
    }

    model.removeStockFromPortfolio(portfolio, ticker);
    view.printMessage(String.format("Successfully added stock %s to portfolio %s.", ticker,
            portfolio));
  }
}
