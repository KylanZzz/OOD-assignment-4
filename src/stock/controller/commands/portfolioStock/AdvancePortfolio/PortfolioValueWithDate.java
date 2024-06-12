package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class PortfolioValueWithDate extends StockPortfolioCommand {
  /**
   * Constructs a command with a stock's view, model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param portfolioModel   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public PortfolioValueWithDate(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(portfolioView, portfolioModel, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    portfolioView.printMessage(String.format("What date would you like to know the value of portfolio %s " +
            "at? Please enter the date in the format MM/DD/YYYY.", portfolio));
    LocalDate date = getDateFromUser();

    try {
      double value = portfolioModel.getPortfolioValue(portfolio, date);
      portfolioView.printMessage(String.format("The value of the portfolio %s at %s is %.2f.",
              portfolio, date, value));
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}