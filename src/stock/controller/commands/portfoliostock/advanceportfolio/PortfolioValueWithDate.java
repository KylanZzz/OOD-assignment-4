package stock.controller.commands.portfoliostock.advanceportfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import stock.controller.commands.portfoliostock.StockPortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

/**
 * This command calculates and displays the value of a
 * specified stock portfolio on a given date.
 * The user is prompted to input a date, and the value of
 * the portfolio on that date is then retrieved
 * and displayed.
 */
public class PortfolioValueWithDate extends StockPortfolioCommand {
  /**
   * Constructs a command with a stock's view, model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param model   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public PortfolioValueWithDate(StockView view, StockModel model,
                                Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;

    portfolioView.printMessage(String.format("What date would you "
            + "like to know the value of portfolio %s "
            + "at?", portfolio));
    LocalDate date = getDateFromUser();

    try {
      double value = portfolioModel.getPortfolioValue(portfolio, date);
      portfolioView.printMessage(String.format("The value of the portfolio %s at %s is $%.2f.",
              portfolio, date, value));
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
