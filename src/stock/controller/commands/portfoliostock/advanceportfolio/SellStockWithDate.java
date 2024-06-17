package stock.controller.commands.portfoliostock.advanceportfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;
import stock.controller.commands.portfoliostock.StockPortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

/**
 * Command class responsible for selling stocks from a specified portfolio on a given date.
 * This command facilitates user interaction for stock sale transactions based
 * on specified parameters.
 */
public class SellStockWithDate extends StockPortfolioCommand {

  /**
   * Constructs a command with a stock's view, model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param model   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public SellStockWithDate(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;

    portfolioView.printMessage("Please enter the date that you want to "
            + "sell the stocks in the format MM/DD/YYYY: ");
    LocalDate date = getDateFromUser();

    if (portfolioModel.getPortfolioContentsDecimal(portfolio, date).isEmpty()) {
      view.printMessage(String.format("You haven't buy any stock to this portfolio at %s", date));
      return;
    }

    portfolioView.printMessage(String.format("Please enter the ticker of the stock "
            + "that you would like to sell from portfolio %s:", portfolio));
    String ticker = getTickerInPortfolioFromUser(date);

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

    try {
      portfolioModel.sellStockFromPortfolio(portfolio, ticker, shares, date);
      portfolioView.printMessage(String.format("Successfully sold %d number "
              + "of %s stocks from date %s in the %s "
              + "portfolio.", shares, ticker, date, portfolio));
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
