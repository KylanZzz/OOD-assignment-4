package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class CalculateGain extends PortfolioCommand {
  public CalculateGain(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner) {
    super(portfolioView, portfolioModel, scanner);
  }

  @Override
  public void apply() {
    portfolioView.printMessage("Please enter the ticker of the stock that you would like to know about:");
    String ticker = getTickerFromUser();

    portfolioView.printMessage("Please enter the starting date (inclusive) in the format MM/DD/YYYY:");
    LocalDate startDate = getDateFromUser();

    portfolioView.printMessage("Please enter the ending date (inclusive) in the format MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    if (!endDate.isAfter(startDate)) {
      portfolioView.printMessage("Invalid input: The end date must be after the start date.");
      return;
    }

    try {
      double gain = portfolioModel.getGainOverTime(startDate, endDate, ticker);
      portfolioView.printStockGain(ticker, startDate, endDate, gain);
    } catch (IOException e) {
      portfolioView.printMessage("Error while fetching data: " + e.getMessage());
    }
  }
}
