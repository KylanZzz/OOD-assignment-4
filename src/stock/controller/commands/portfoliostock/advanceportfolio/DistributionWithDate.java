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
 * Command class responsible for displaying the distribution of a
 * stock portfolio's value at a specified date.
 * It extends the StockPortfolioCommand which provides basic command structure.
 * This class handles user input to fetch and display the value
 * distribution of the portfolio on a given date.
 */
public class DistributionWithDate extends StockPortfolioCommand {

  /**
   * Constructs a DisplayPortfolio command object.
   *
   * @param view     The view used to interact with the user.
   * @param model    The model used for portfolio data manipulation.
   * @param scanner  The scanner to read user input.
   * @param portfolio The name of the portfolio to display.
   */
  public DistributionWithDate(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;

    portfolioView.printMessage(String.format("What date would you like"
            + " to know the value of portfolio %s "
            + "at? Please enter the date in the format MM/DD/YYYY.", portfolio));
    LocalDate date = getDateFromUser();

    try {
      portfolioView.printDistribution(portfolioModel
              .getPortfolioDistribution(portfolio, date), portfolio, date);
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }


}
