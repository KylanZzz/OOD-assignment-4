package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;

public class RebalancePortfolio extends StockPortfolioCommand {
  public RebalancePortfolio(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(portfolioView, portfolioModel, scanner, portfolio);
  }

  @Override
  public void apply() {
    portfolioView.printMessage("Please enter the name of the portfolio "
            + "that you would like to rebalance: ");
    String name = getPortfolioNameFromUserWOSave();

    portfolioView.printMessage("Please enter the date "
            + "that you would like to rebalance for: ");

    LocalDate date = getDateFromUser();

    portfolioView.printMessage("Please enter the proportions "
            + "that you would like to rebalance for: ");

    Map<String, Double> proportions = new HashMap<>();
    try {
      portfolioModel.rebalancePortfolio(name, date, proportions);
      portfolioView.printDistribution(proportions, name, date);
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }

  }
}
