package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class CalculateAverage extends PortfolioCommand {
  /**
   * Constructs a calculate average command with a stock's view, model,
   * and source of input.
   *
   * @param portfolioView    the view of the stock program.
   * @param portfolioModel   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public CalculateAverage(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(portfolioView, portfolioModel, scanner);
  }

  /**
   * Prompts the user for the date, stock ticker, and number of days, then calculates
   * and displays the the user the average price of the stock over the last x-days.
   * If the ticker is invalid, an error is displayed and the user is prompted again.
   * If the date is invalid, an error is displayed and the user is prompted again.
   * If the number of days is invalid (non-positive or not an integer) then an error
   * is displayed and the user is prompted again.
   */
  @Override
  public void apply() {
    portfolioView.printMessage("Please enter the ticker of the stock that you would like to know about:");
    String ticker = getTickerFromUser();

    portfolioView.printMessage("Please enter the ending date in the format MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    portfolioView.printMessage("Please enter the number of days.");
    int days = getPositiveFromUser(Integer.MAX_VALUE);

    try {
      double average = portfolioModel.getMovingDayAverage(endDate, days, ticker);
      portfolioView.printStockAverage(ticker, endDate, days, average);
    } catch (IOException e) {
      portfolioView.printMessage("Error while fetching data: " + e.getMessage());
    }
  }
}