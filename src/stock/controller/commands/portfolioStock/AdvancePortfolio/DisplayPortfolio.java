package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.time.LocalDate;
import java.util.Scanner;

import stock.controller.commands.portfolioStock.PortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;

public class DisplayPortfolio extends StockPortfolioCommand {
  public DisplayPortfolio(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(portfolioView, portfolioModel, scanner, portfolio);
  }

  @Override
  public void apply() {
    portfolioView.printMessage("Enter the date that you add the stocks: ");
    LocalDate date = getDateFromUser();

    portfolioView.printManagePortfolioDouble(portfolioModel.getPortfolioContentsDecimal(portfolio, date), portfolio, date);

  }
}
