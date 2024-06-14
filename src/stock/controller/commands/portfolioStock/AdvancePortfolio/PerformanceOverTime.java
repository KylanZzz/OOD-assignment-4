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


    portfolioView.printMessage("Please enter the starting date (inclusive) in the format "
            + "MM/DD/YYYY:");
    LocalDate startDate = getDateFromUser();

    portfolioView.printMessage("Please enter the ending date (inclusive) in the format "
            + "MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    try {
      portfolioView.printPortfolioPerformance(portfolioModel.getPortfolioPerformance(portfolio, startDate, endDate), startDate, endDate);
      if (portfolioModel.getPortfolioPerformance(portfolio, startDate, endDate).isEmpty()) {
        return;
      }
      portfolioView.printMessage(String.format("Performance of portfolio %s from %s to %s", portfolio, startDate, endDate));
      portfolioView.printMessage("");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


  }
}
