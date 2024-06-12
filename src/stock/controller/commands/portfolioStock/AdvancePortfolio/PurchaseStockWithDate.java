package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import stock.controller.commands.Command;
import stock.controller.commands.portfolio.CreatePortfolio;
import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.model.PortfolioStockModelImpl;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class PurchaseStockWithDate extends StockPortfolioCommand {


  /**
   * Constructs a command with a stock's view, model, and source of input.
   *
   * @param portfolioView    the view of the stock program.
   * @param portfolioModel   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public PurchaseStockWithDate(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(portfolioView, portfolioModel, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
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

    portfolioView.printMessage("Please enter the date that you want to purchase the stocks: ");
    LocalDate date = getDateFromUser();
    try {
      portfolioModel.addStockToPortfolio(portfolio, ticker, shares, date);
      portfolioView.printMessage(String.format("Successfully purchased %d number of %s stocks at date %s in the %s "
              + "portfolio.", shares, ticker, date, portfolio));
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
