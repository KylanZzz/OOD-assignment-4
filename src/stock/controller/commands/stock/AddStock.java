package stock.controller.commands.stock;

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
            "that you would like to add to portfolio %s.", portfolio));
    String ticker = scanner.nextLine().toUpperCase();
    if (!model.stockExists(ticker)) {
      view.printMessage("That stock does not exist!");
      return;
    }

    if (model.getPortfolioContents(portfolio).contains(ticker)) {
      view.printMessage("That stock is already in the portfolio!");
      return;
    }

    model.addStockToPortfolio(portfolio, ticker);
    view.printMessage(String.format("Successfully added stock %s to portfolio %s.", ticker,
            portfolio));
  }
}
