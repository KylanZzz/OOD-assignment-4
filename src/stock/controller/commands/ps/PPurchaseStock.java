package stock.controller.commands.ps;

import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import stock.controller.commands.stock.AddStock;
import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.BasicPortfolioStockView;
import stock.view.StockView;

public class PPurchaseStock extends AddStock {
//  private PortfolioStockModel model;

  /**
   * Constructs an add stock command with a stock's view,
   * model, and source of input.
   *
   * @param view      the view of the stock program.
   * @param model     the model of the stock program.
   * @param scanner   the input of the stock program.
   * @param portfolio the name of the portfolio.
   */
  public PPurchaseStock(StockView view, StockModel  model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
//    this.model = model;
  }

  @Override
  public void apply() {
    if (!(model instanceof PortfolioStockModel)) {
      view.printMessage("Model is not compatible.");
      return;
    }
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;

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

    view.printMessage("Please enter the date that you want to purchase the stocks: ");
    LocalDate date = getDateFromUser();

    try {
      portfolioModel.addStockToPortfolio(portfolio, ticker, shares, date);
      view.printMessage(String.format("Successfully purchased %d shares of %s on %s in the %s portfolio.",
              shares, ticker, date.toString(), portfolio));
    } catch (IOException | IllegalArgumentException e) {
      view.printMessage("Error: " + e.getMessage());
    }

  }
}
