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
    String ticker = getTickerFromUser();

    if (!model.getPortfolioContents(portfolio).containsKey(ticker)) {
      view.printMessage("That stock is not in the portfolio.");
      return;
    }

    model.removeStockFromPortfolio(portfolio, ticker);
    view.printMessage(String.format("Successfully removed stock %s from portfolio %s.", ticker,
            portfolio));
  }
}
