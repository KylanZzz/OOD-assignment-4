package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.view.StockView;

public class DistributionWithDate extends StockPortfolioCommand {


  public DistributionWithDate(StockView view, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(view, portfolioModel, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    view.printMessage(String.format("What date would you like to know the value of portfolio %s " +
            "at? Please enter the date in the format MM/DD/YYYY.", portfolio));
    LocalDate date = getDateFromUser();

    try {
      portfolioModel.getPortfolioDistribution(portfolio, date);
      view.printDistribution(portfolioModel.getPortfolioContentsDecimal(portfolio, date), portfolio, date);

    } catch (IOException e) {
      view.printMessage("Error occurred while fetching data: " + e.getMessage());
    }

  }
}
