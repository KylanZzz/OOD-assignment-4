package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import stock.controller.commands.Command;
import stock.controller.commands.portfolio.CreatePortfolio;
import stock.controller.commands.portfolioStock.StockPortfolioCommand;
import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.model.PortfolioStockModelImpl;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class PurchaseStockWithDate extends StockPortfolioCommand {


  /**
   * Constructs a command with a stock's view, model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param model   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public PurchaseStockWithDate(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;

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

    portfolioView.printMessage("Please enter the date in the format MM/DD/YYYY: ");
    LocalDate date = getDateFromUser();
    try {
      portfolioModel.addStockToPortfolio(portfolio, ticker, shares, date);
      portfolioView.printMessage(String.format("Successfully purchased %d shares of %s stocks at date %s in the %s "
              + "portfolio.", shares, ticker, date, portfolio));
      portfolioView.printMessage("");
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
