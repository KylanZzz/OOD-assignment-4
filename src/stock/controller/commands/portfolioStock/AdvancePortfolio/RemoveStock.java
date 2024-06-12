package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;

public class RemoveStock extends StockPortfolioCommand{
  public RemoveStock(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(portfolioView, portfolioModel, scanner, portfolio);
  }

  @Override
  public void apply() {
    portfolioView.printMessage(String.format("Please enter the ticker of the stock "
            + "that you would like to remove from portfolio %s.", portfolio));
    String ticker = getTickerFromUser();

    if (!portfolioModel.getPortfolioContents(portfolio).containsKey(ticker)) {
      portfolioView.printMessage("That stock is not in the portfolio.");
      return;
    }

    portfolioModel.removeStockFromPortfolio(portfolio, ticker);
    portfolioView.printMessage(String.format("Successfully removed stock %s from portfolio %s.", ticker,
            portfolio));
  }
}
