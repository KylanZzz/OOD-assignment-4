package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import stock.controller.commands.portfolioStock.StockPortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

/**
 * Command class that calculates and displays the performance
 * of a stock portfolio over a given time period.
 * It prompts the user to input a start and end date and then
 * retrieves and displays the performance between these dates.
 */
public class PerformanceOverTime extends StockPortfolioCommand {

  /**
   * Constructs a DisplayPortfolio command object.
   *
   * @param view     The view used to interact with the user.
   * @param model    The model used for portfolio data manipulation.
   * @param scanner  The scanner to read user input.
   * @param portfolio The name of the portfolio to display.
   */
  public PerformanceOverTime(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  @Override
  public void apply() {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;


    portfolioView.printMessage("Please enter the starting date (inclusive) in the format "
            + "MM/DD/YYYY:");
    LocalDate startDate = getDateFromUser();

    portfolioView.printMessage("Please enter the ending date (inclusive) in the format "
            + "MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    try {
      portfolioView.printPortfolioPerformance(portfolioModel
              .getPortfolioPerformance(portfolio, startDate, endDate), startDate, endDate);
      if (portfolioModel.getPortfolioPerformance(portfolio, startDate, endDate).isEmpty()) {
        return;
      }
      portfolioView.printMessage(String.format("Performance of portfolio %s from %s to %s",
              portfolio, startDate, endDate));
      portfolioView.printMessage("");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


  }
}
