package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.util.InputMismatchException;
import java.util.Scanner;

import stock.controller.BasicStockController;
import stock.controller.StockController;
import stock.controller.commands.Command;
import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;

public class AddStock extends StockPortfolioCommand {

  public AddStock(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(portfolioView, portfolioModel, scanner, portfolio);
  }

  @Override
  public void apply() {
    portfolioView.printMessage(String.format("Please enter the ticker of the stock "
            + "that you would like to add to portfolio %s:", portfolio));
    String ticker = getTickerFromUser();

    portfolioView.printMessage("Please enter the number of shares you would like to "
            + "purchase (you cannot buy fractional number of stocks): ");
    int shares;
    try {
      shares = scanner.nextInt();
      scanner.nextLine();
    } catch (InputMismatchException e) {
      portfolioView.printMessage("Invalid input: not an integer, please try again.");
      scanner.nextLine();
      return;
    }

    if (shares == 0) {
      portfolioView.printMessage("Cannot purchase 0 shares of a stock.");
      return;
    }

    if (shares < 0) {
      portfolioView.printMessage("Cannot purchase negative number of stocks.");
      return;
    }

    portfolioModel.addStockToPortfolio(portfolio, ticker, shares);
    portfolioView.printMessage(String.format("Successfully purchased %d number of %s stocks in the %s "
            + "portfolio.", shares, ticker, portfolio));
  }
}
