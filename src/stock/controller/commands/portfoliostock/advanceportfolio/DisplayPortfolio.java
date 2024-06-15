package stock.controller.commands.portfoliostock.advanceportfolio;

import java.time.LocalDate;
import java.util.Scanner;
import stock.controller.commands.portfoliostock.StockPortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

/**
 * Command class responsible for displaying the composition of a specific stock portfolio.
 * It extends the StockPortfolioCommand which provides basic command structure.
 * This class handles user input to fetch and display the composition
 * of the portfolio at a specific date.
 */
public class DisplayPortfolio extends StockPortfolioCommand {

  /**
   * Constructs a DisplayPortfolio command object.
   *
   * @param view     The view used to interact with the user.
   * @param model    The model used for portfolio data manipulation.
   * @param scanner  The scanner to read user input.
   * @param portfolio The name of the portfolio to display.
   */
  public DisplayPortfolio(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  /**
   * Executes the action of displaying the portfolio's composition.
   * Prompts the user for a date and displays the composition of the portfolio
   * on that date. The date format expected is MM/DD/YYYY.
   * It uses the for retrieving the portfolio data and
   * PortfolioStockView for displaying information to the user.
   */
  @Override
  public void apply() {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;

    portfolioView.printMessage(String.format("What date would "
            + "you like to know the composition of portfolio %s"
            + " at? Please enter the date in the format MM/DD/YYYY ", portfolio));
    LocalDate date = getDateFromUser();

    portfolioView.printManagePortfolioDouble(portfolioModel
            .getPortfolioContentsDecimal(portfolio, date), portfolio);

  }
}
