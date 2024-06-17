package stock.controller.commands.portfoliostock.advanceportfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;
import stock.controller.commands.portfoliostock.StockPortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

/**
 * Command class responsible for rebalancing a specified
 * portfolio based on desired stock proportions
 * on a given date. It prompts the user for the rebalance
 * date and the new stock proportions, then
 * updates the portfolio accordingly.
 */
public class RebalancePortfolio extends StockPortfolioCommand {

  /**
   * Constructs a DisplayPortfolio command object.
   *
   * @param view     The view used to interact with the user.
   * @param model    The model used for portfolio data manipulation.
   * @param scanner  The scanner to read user input.
   * @param portfolio The name of the portfolio to display.
   */
  public RebalancePortfolio(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  @Override
  public void apply() {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;


    portfolioView.printMessage("What date would you like to rebalance for? "
            + "Please enter the date in the format MM/DD/YYYY: ");
    LocalDate date = getDateFromUser();

    Map<String, Double> proportions = getProportionsFromUser(date);

    try {
      portfolioModel.rebalancePortfolio(portfolio, date, proportions);
      portfolioView.printDistribution(portfolioModel
              .getPortfolioDistribution(portfolio, date), portfolio, date);
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
