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


    portfolioView.printMessage("What date would you like to rebalance for? Please enter the date in the format MM/DD/YYYY: ");
    LocalDate date = getDateFromUser();

    Map<String, Double> proportions = getProportionsFromUser(date);

    try {
      portfolioModel.rebalancePortfolio(portfolio, date, proportions);
      portfolioView.printDistribution(portfolioModel.getPortfolioDistribution(portfolio, date), portfolio, date);
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }

  }
}
