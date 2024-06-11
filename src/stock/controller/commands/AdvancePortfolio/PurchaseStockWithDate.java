package stock.controller.commands.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import stock.controller.commands.Command;
import stock.controller.commands.portfolio.CreatePortfolio;
import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.model.PortfolioStockModelImpl;
import stock.view.StockView;

public class PurchaseStockWithDate extends StockCommand {


  /**
   * Constructs a command with a stock's view, model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param portfolioModel   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public PurchaseStockWithDate(StockView view, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(view, portfolioModel, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    view.printMessage(String.format("Please enter the ticker of the stock "
            + "that you would like to add to portfolio %s:", portfolio));
    String ticker = getTickerFromUser();

    view.printMessage("Please enter the number of shares you would like to "
            + "purchase (you cannot buy fractional number of stocks): ");
    int shares;
    try {
      shares = scanner.nextInt();
      scanner.nextLine();
    } catch (InputMismatchException e) {
      view.printMessage("Invalid input: not an integer, please try again.");
      scanner.nextLine();
      return;
    }
    if (shares == 0) {
      view.printMessage("Cannot purchase 0 shares of a stock.");
      return;
    }

    if (shares < 0) {
      view.printMessage("Cannot purchase negative number of stocks.");
      return;
    }

    LocalDate date = getDateFromUser();
    try {
      portfolioModel.addStockToPortfolio(portfolio, ticker, shares, date);
      view.printMessage(String.format("Successfully purchased %d number of %s stocks in the %s "
              + "portfolio at date %s.", shares, ticker, portfolio, date));
    } catch (IOException e) {
      view.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
