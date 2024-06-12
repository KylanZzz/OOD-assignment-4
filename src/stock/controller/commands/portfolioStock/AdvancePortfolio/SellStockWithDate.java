package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class SellStockWithDate extends StockPortfolioCommand {

  /**
   * Constructs a command with a stock's view, model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param portfolioModel   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public SellStockWithDate(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
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
            + "sell (you cannot sell fractional number of stocks): ");

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
      portfolioView.printMessage("Cannot sell 0 shares of a stock.");
      return;
    }
    if (shares < 0) {
      portfolioView.printMessage("Cannot sell negative number of stocks.");
      return;
    }
    portfolioView.printMessage("Please enter the date that you want to sell the stocks: ");
    LocalDate date = getDateFromUser();

    try {
      portfolioModel.sellStockFromPortfolio(portfolio, ticker, shares, date);
      portfolioView.printMessage(String.format("Successfully sold %d number of %s stocks at date %s in the %s "
              + "portfolio.", shares, ticker, date, portfolio));
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }

  }
}