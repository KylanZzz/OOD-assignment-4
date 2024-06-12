package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;

public class PerformanceOverTime extends StockPortfolioCommand {

  public PerformanceOverTime(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(portfolioView, portfolioModel, scanner, portfolio);
  }

  @Override
  public void apply() {
    portfolioView.printMessage("Please enter the name of the portfolio "
            + "that you would like to see the performance: ");
    String name = getPortfolioNameFromUserWOSave();

    portfolioView.printMessage("Please enter the start date: ");
    LocalDate startDate = getDateFromUser();

    portfolioView.printMessage("Please enter the end date: ");
    LocalDate endDate = getDateFromUser();

    try {
      portfolioView.printPortfolioPerformance(portfolioModel.getPortfolioPerformance(name, startDate, endDate), startDate, endDate);
      portfolioView.printMessage(String.format("Performance of portfolio %s from %s to %s", name, startDate, endDate));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }


  }
}
