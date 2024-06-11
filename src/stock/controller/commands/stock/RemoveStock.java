package stock.controller.commands.stock;

import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.StockView;

/**
 * Command to remove a stock from a portfolio.
 */
public class RemoveStock extends StockCommand {

  /**
   * Constructs a remove stock command with a stock's view,
   * model, and source of input.
   *
   * @param view the view of the stock program.
   * @param portfolioModel the model of the stock program.
   * @param scanner the input of the stock program.
   * @param portfolio the name of the portfolio.
   */
//  public RemoveStock(StockView view, StockModel model, Scanner scanner, String portfolio) {
//    super(view, model, scanner, portfolio);
//  }
  public RemoveStock(StockView view, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(view, portfolioModel, scanner, portfolio);
  }


  /**
   * Prompts the user for the ticker of the stock they would like to
   * remove from the portfolio, then removes the stock
   * from the portfolio.
   * If the ticker is not valid, then an error is displayed and the
   * user is prompted again.
   * If the stock does not exist in the portfolio, then the command
   * will terminate and an error message displayed.
   */
  @Override
  public void apply() {
    view.printMessage(String.format("Please enter the ticker of the stock "
            + "that you would like to remove from portfolio %s.", portfolio));
    String ticker = getTickerFromUser();

    if (!portfolioModel.getPortfolioContents(portfolio).containsKey(ticker)) {
      view.printMessage("That stock is not in the portfolio.");
      return;
    }

    portfolioModel.removeStockFromPortfolio(portfolio, ticker);
    view.printMessage(String.format("Successfully removed stock %s from portfolio %s.", ticker,
            portfolio));
  }
}