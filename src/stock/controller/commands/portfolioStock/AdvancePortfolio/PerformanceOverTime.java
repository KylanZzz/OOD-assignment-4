package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.controller.commands.portfolioStock.StockPortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class PerformanceOverTime extends StockPortfolioCommand {

  public PerformanceOverTime(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  @Override
  public void apply() {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;

    portfolioView.printMessage("Please enter the name of the portfolio "
            + "that you would like to see the performance: ");
    String name = getPortfolioNameFromUserWOSave();

    portfolioView.printMessage("Please enter the starting date (inclusive) in the format "
            + "MM/DD/YYYY:");
    LocalDate startDate = getDateFromUser();

    portfolioView.printMessage("Please enter the ending date (inclusive) in the format "
            + "MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    try {
      portfolioView.printPortfolioPerformance(portfolioModel.getPortfolioPerformance(name, startDate, endDate), startDate, endDate);
      portfolioView.printMessage(String.format("Performance of portfolio %s from %s to %s", name, startDate, endDate));
      portfolioView.printMessage("");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


  }
}
