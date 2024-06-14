package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import stock.controller.commands.portfolioStock.StockPortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class RebalancePortfolio extends StockPortfolioCommand {
  public RebalancePortfolio(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  @Override
  public void apply() {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;

    portfolioView.printMessage("Please enter the name of the portfolio "
            + "that you would like to rebalance: ");
    String name = getPortfolioNameFromUserWOSave();

    portfolioView.printMessage("What date would you like to rebalance for? Please enter the date in the format MM/DD/YYYY.: ");

    LocalDate date = getDateFromUser();

    portfolioView.printMessage("What proportions would you want to rebalance to? "
            + "Please enter the proportion in the decimal format : ");

    Map<String, Double> proportions = new HashMap<>();
    try {
      portfolioModel.rebalancePortfolio(name, date, proportions);
      portfolioView.printDistribution(proportions, name, date);
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }

  }
}
