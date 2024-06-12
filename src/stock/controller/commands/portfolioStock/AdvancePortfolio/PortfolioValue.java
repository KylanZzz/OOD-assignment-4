package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;

public class PortfolioValue extends StockPortfolioCommand{
  public PortfolioValue(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(portfolioView, portfolioModel, scanner, portfolio);
  }

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
